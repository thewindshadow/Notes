elasticsearch



索引操作

~~~shell
查询所有的索引
curl -X GET "localhost:9200/_cat/indices?v" 查询所有的索引index

创建一个customer 索引
curl -X PUT "localhost:9200/customer/_doc/1?pretty" -H'Content-Type: application/json' 
-d'{"name": "John Doe"}' 

查询id为1的文档
curl -X GET "localhost:9200/customer/_doc/1?pretty"

删除索引
curl -X DELETE "localhost:9200/customer?pretty"
~~~

API的格式

~~~shell
curl -X <HTTP Verb>/<Index>/<Type>/<ID>
~~~



文档操作



查询文档

~~~shell
curl -X GET "localhost:9200/customer/_doc/1?pretty"
~~~



添加文档

~~~shell
curl -X PUT "localhost:9200/customer/_doc/1?pretty" -H'Content-Type:application/json' 
-d'{"name":"ouYang"}'
	
注意：-H后面与-d后面不留空格
~~~

更新文档

~~~shell
修改字段的值
curl -X POST "localhost:9200/customer/_doc/1/_update?pretty" 
-H'Content-Type:application/json' -d'{"doc":{"name":"张三"}}'


更新文档是添加新的字段
curl -X POST "localhost:9200/customer/_doc/1/_update?pretty" 
-H'Content-Type:application/json' -d'{"script":"ctx._source.age += 5"}'


利用脚本进行执行
curl -X POST "localhost:9200/customer/_doc/1/_update?pretty" -H'Content-Type:application/json' 
-d'{"script":"ctx._source.age += 5"}'

~~~

删除文档

~~~shell
curl -X DELETE "localhost:9200/customer/_doc/1?pretty"
~~~

批处理

除了对单个的文档进行索引、更新和删除，Elasticsearch也提供了相关操作的批处理功能，这些批处理功能通过使用_bulk API实现。通过批处理可以非常高效的完成多个文档的操作，同时可以减少不必要的网络请求。 

