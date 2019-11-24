package cw

import com.mongodb.BasicDBObject
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import groovyx.net.http.URIBuilder
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import io.reactivex.Single

import javax.inject.Inject

/**
 * Created by chris on 21/11/19.
 */

@Controller
class SearchController {

    @Inject MongoClient mongoClient

    @Get("/find")
    Single<List<Program>> find(HttpRequest request) {
        Map params = getParams(request.getUri().toString())
        String findQueryString = findQueryBuilder(params)
        String sortQueryString = sortQueryBuilder(params)

        Integer pageSize = params.pageSize.toInteger()
        Integer pageNumber = params.pageNumber.toInteger()

        BasicDBObject findQuery = BasicDBObject.parse(findQueryString)
        BasicDBObject sortQuery = BasicDBObject.parse(sortQueryString)

        return Flowable.fromPublisher(
                getCollection()
                        .find(findQuery)
                        .skip(pageSize * (pageNumber - 1))
                        .limit(pageSize)
                        .sort(sortQuery)
        ).toList()
    }

    private MongoCollection<Program> getCollection() {
        return mongoClient
                .getDatabase("bbc")
                .getCollection("programs", Program.class)
    }

    private static String findQueryBuilder(Map params) {
        StringBuilder query = new StringBuilder("{")

        if (params?.pid) {
            query << "pid:/${params.pid}/,"
        }

        if (params?.start_time_from && params?.start_time_to) {
            query << "start_time:{"
            query <<    "\$gte:${params?.start_time_from},"
            query <<    "\$lt:${params?.start_time_to}"
            query << "},"
        }

        if (params?.end_time_from && params?.end_time_to) {
            query << "end_time: {"
            query <<    "\$gte:${params?.end_time_from},"
            query <<    "\$lt:${params?.end_time_to}"
            query << "},"
        }

        // %Y-%m-%dT%H:%M:%S.%LZ format to search with
        if (params?.epoch_start_from && params?.epoch_start_to) {
            query << "epoch_start: {"
            query <<    "\$gte:{"
            query <<        "\$date:${params?.epoch_start_from}"
            query <<    "},"
            query <<    "\$lt:{"
            query <<        "\$date:${params?.epoch_start_to}"
            query <<    "}"
            query << "},"
        }

        if (params?.epoch_end_from && params?.epoch_end_to) {
            query << "epoch_end: {"
            query <<    "\$gte:{"
            query <<        "\$date:${params?.epoch_end_from}"
            query <<    "},"
            query <<    "\$lt:{"
            query <<        "\$date:${params?.epoch_end_to}"
            query <<    "}"
            query << "},"
        }

        if (params?.complete_title) {
            query << "complete_title:/${params.complete_title}/,"
        }

        if (params?.media_type) {
            query << "media_type:/${params.media_type}/,"
        }

        if (params?.masterbrand) {
            query << "masterbrand:/${params.masterbrand}/,"
        }

        if (params?.service) {
            query << "service:/${params.service}/,"
        }

        if (params?.brand_pid) {
            query << "brand_pid:/${params.brand_pid}/,"
        }

        if (params?.is_clip) {
            query << "is_clip:${params.is_clip},"
        }

        if (params?.categories) {
            String regexifiedName = regexify(params?.categories)
            query << "categories:{"
            query <<    "\$regex: /${regexifiedName}/"
            query << "},"
        }

        if (params?.tags) {
            String regexifiedName = regexify(params?.tags)

            query << "tags:{"
            query <<    "\$regex: /${regexifiedName}/"
            query << "},"
        }

        if (params._id) {
            query << "_id:{"
            query <<    "\$oid:\'${params._id}\'"
            query << "},"
        }

        query << "}"

        return query.toString().trim()
    }

    private static String sortQueryBuilder(Map params) {
        StringBuilder query = new StringBuilder("{")

        //ToDo: sort for every detail available

        if (params.order_start_time) {
            query << "start_time: ${params.order_start_time}"
        }

        query << "}"

        return query.toString().trim()
    }

    private static Map getParams(String url) {
        URIBuilder builder = new URIBuilder(url)
        return builder.getQuery()
    }

    private static String regexify(String string) {
        if (!string) return ""
        def regexifiedName = ""
        def namesList = string.replaceAll("\\s","").tokenize(',')
        namesList.each { currName ->
            regexifiedName += "(?=.*${currName})"
        }
        return regexifiedName
    }
}
