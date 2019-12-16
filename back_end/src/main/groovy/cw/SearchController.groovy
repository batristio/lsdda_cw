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
        String findQueryString = new QueryBuilder().getFindQuery(params)
        String sortQueryString = new QueryBuilder().getSortQuery(params)

        Integer pageSize = params?.limit?.toInteger() ?: 10
        Integer pageNumber = params?.page_number?.toInteger() ?: 1

        //ToDo: Deploy on AWS ^

        BasicDBObject findQuery = BasicDBObject.parse(findQueryString)
        BasicDBObject sortQuery = BasicDBObject.parse(sortQueryString)

        println("Find Query --> db.programs.find(${findQueryString})")
        println("Sort Query --> db.programs.sort(${sortQueryString})")

        return Flowable.fromPublisher(
                getCollection()
                        .find(findQuery)
                        .limit(pageSize)
                        .sort(sortQuery)
        ).toList()
    }

    @Get('/findCount')
    Single<List<Long>> findCount(HttpRequest request) {
        Map params = getParams(request.getUri().toString())
        String findQueryString = new QueryBuilder().getFindQuery(params)

        BasicDBObject findQuery = BasicDBObject.parse(findQueryString)

        println("Count Query -> db.programs.find(${findQueryString}).count()")

        return Flowable.fromPublisher(
                getCollection()
                    .countDocuments(findQuery)
        ).toList()
    }

    private MongoCollection<Program> getCollection() {
        return mongoClient
                .getDatabase("bbc")
                .getCollection("programs", Program.class)
    }

    private static Map getParams(String url) {
        if (!url) return [:]
        return new URIBuilder(url).getQuery()
    }
}
