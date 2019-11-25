package cw

/**
 * Created by chris on 24/11/19.
 */
class QueryBuilder {

    private String find = ""
    private String sort = ""

    public getFindQuery(Map params) {
        return find(params)
    }

    public getSortQuery(Map params) {
        return sort(params)
    }

    private static String find(Map params) {
        StringBuilder query = new StringBuilder("{")

        if (params?.pid) {
            query << "pid:/${params.pid}/,"
        }

        if (params?.start_time_from && params?.start_time_to) {
            query << "start_time:{"
            query <<    "\$gte:${params?.start_time_from},"
            query <<    "\$lt:${params?.start_time_to}"
            query << "},"
        } else if (params?.start_time_from) {
            query << "start_time:{"
            query <<    "\$gte:${params?.start_time_from}"
            query << "},"
        } else if (params?.start_time_to) {
            query << "start_time:{"
            query <<    "\$lte:${params?.start_time_to}"
            query << "},"
        }

        if (params?.end_time_from && params?.end_time_to) {
            query << "end_time: {"
            query <<    "\$gte:${params?.end_time_from},"
            query <<    "\$lt:${params?.end_time_to}"
            query << "},"
        } else if (params?.end_time_from) {
            query << "end_time: {"
            query <<    "\$gte:${params?.end_time_from}"
            query << "},"
        } else if (params?.end_time_to) {
            query << "end_time: {"
            query <<    "\$lte:${params?.end_time_to}"
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
        } else if (params?.epoch_start_from) {
            query << "epoch_start: {"
            query <<    "\$gte:{"
            query <<        "\$date:${params?.epoch_start_from}"
            query <<    "}"
            query << "},"
        } else if (params?.epoch_start_to) {
            query << "epoch_start: {"
            query <<    "\$lte:{"
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
        } else if (params?.epoch_end_from) {
            query << "epoch_end: {"
            query <<    "\$gte:{"
            query <<        "\$date:${params?.epoch_end_from}"
            query <<    "}"
            query << "},"
        } else if (params?.epoch_end_to) {
            query << "epoch_end: {"
            query <<    "\$lte:{"
            query <<        "\$date:${params?.epoch_end_to}"
            query <<    "}"
            query << "},"
        }

        if (params?.complete_title) {
            query << "complete_title:/${params?.complete_title}/,"
        }

        if (params?.media_type) {
            query << "media_type:/${params?.media_type}/,"
        }

        if (params?.masterbrand) {
            query << "masterbrand:/${params?.masterbrand}/,"
        }

        if (params?.service) {
            query << "service:/${params?.service}/,"
        }

        if (params?.brand_pid) {
            query << "brand_pid:/${params?.brand_pid}/,"
        }

        if (params?.is_clip) {
            query << "is_clip:${params?.is_clip},"
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

        if (params?._id) {
            query << "_id:{"
            query <<    "\$oid:\'${params?._id}\'"
            query << "},"
        }

        query << "}"

        return query.toString().trim()
    }

    private static String sort(Map params) {
        StringBuilder query = new StringBuilder("{")

        if (params?.order_pid) {
            query << "pid: ${params?.order_pid},"
        }

        if (params?.order_start_time) {
            query << "start_time: ${params?.order_start_time},"
        }

        if (params?.order_end_time) {
            query << "end_time: ${params?.order_end_time},"
        }

        if (params?.order_epoch_start) {
            query << "epoch_start: ${params?.order_epoch_start},"
        }

        if (params?.order_epoch_end) {
            query << "epoch_end: ${params?.order_epoch_end},"
        }

        if (params?.order_complete_title) {
            query << "complete_title: ${params?.order_complete_title},"
        }

        if (params?.order_media_type) {
            query << "media_type: ${params?.order_media_type},"
        }

        if (params?.order_masterbrand) {
            query << "masterbrand: ${params?.order_masterbrand},"
        }

        if (params?.order_service) {
            query << "service: ${params?.order_service},"
        }

        if (params?.order_brand_pid) {
            query << "brand_pid: ${params?.order_brand_pid},"
        }

        if (params?.order_is_clip) {
            query << "is_clip: ${params?.order_is_clip},"
        }

        if (params?.order_categories) {
            query << "categories: ${params?.order_categories},"
        }

        if (params?.order_tags) {
            query << "tags: ${params?.order_tags},"
        }

        query << "}"

        return query.toString().trim()
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
