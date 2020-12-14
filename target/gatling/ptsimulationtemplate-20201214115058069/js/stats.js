var stats = {
    type: "GROUP",
name: "Global Information",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "Global Information",
    "numberOfRequests": {
        "total": "10",
        "ok": "0",
        "ko": "10"
    },
    "minResponseTime": {
        "total": "1426",
        "ok": "-",
        "ko": "1426"
    },
    "maxResponseTime": {
        "total": "1606",
        "ok": "-",
        "ko": "1606"
    },
    "meanResponseTime": {
        "total": "1514",
        "ok": "-",
        "ko": "1514"
    },
    "standardDeviation": {
        "total": "74",
        "ok": "-",
        "ko": "74"
    },
    "percentiles1": {
        "total": "1521",
        "ok": "-",
        "ko": "1521"
    },
    "percentiles2": {
        "total": "1574",
        "ok": "-",
        "ko": "1574"
    },
    "percentiles3": {
        "total": "1606",
        "ok": "-",
        "ko": "1606"
    },
    "percentiles4": {
        "total": "1606",
        "ok": "-",
        "ko": "1606"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "count": 10,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "5",
        "ok": "-",
        "ko": "5"
    }
},
contents: {
"req_getmodel-api-df4b7": {
        type: "REQUEST",
        name: "GetModel-API",
path: "GetModel-API",
pathFormatted: "req_getmodel-api-df4b7",
stats: {
    "name": "GetModel-API",
    "numberOfRequests": {
        "total": "10",
        "ok": "0",
        "ko": "10"
    },
    "minResponseTime": {
        "total": "1426",
        "ok": "-",
        "ko": "1426"
    },
    "maxResponseTime": {
        "total": "1606",
        "ok": "-",
        "ko": "1606"
    },
    "meanResponseTime": {
        "total": "1514",
        "ok": "-",
        "ko": "1514"
    },
    "standardDeviation": {
        "total": "74",
        "ok": "-",
        "ko": "74"
    },
    "percentiles1": {
        "total": "1521",
        "ok": "-",
        "ko": "1521"
    },
    "percentiles2": {
        "total": "1574",
        "ok": "-",
        "ko": "1574"
    },
    "percentiles3": {
        "total": "1606",
        "ok": "-",
        "ko": "1606"
    },
    "percentiles4": {
        "total": "1606",
        "ok": "-",
        "ko": "1606"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "count": 10,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "5",
        "ok": "-",
        "ko": "5"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
