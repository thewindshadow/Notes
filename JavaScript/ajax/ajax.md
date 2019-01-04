## Ajax



~~~html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="http://www.baidu.com">
        ProductId<input type="text" id="productId">
        <button type="button" onclick="mySubmit(event)">查询</button>
    </form>
<script src="./js/jquery.js"></script>
<script>
    function mySubmit(event) {
        var obj = {
            "header": {
                "errorMessage": "",
                "requestChannel": "5",
                "requestType": "103",
                "sendTime": "2015-08-18 16:58:12",
                "sessionId": "20150818165712123",
                "status": "100",
                "version": "1.0"
            },
            "request": {
                "productId": $("#productId").val()
            }
        };
        event.preventDefault();
        $.ajax({
            url: '/request',
            type:"post",
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(obj),
            dataType: "json",
            success: function (data) {
                console.log(data);
            },
            error: function(data){
                console.log(data);
            }
        });
    }
</script>
</body>
</html>
~~~

