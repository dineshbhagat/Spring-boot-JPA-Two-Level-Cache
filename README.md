# Spring-boot-JPA-Two-Level-Cache

Ref:
- https://dzone.com/articles/all-about-hibernate-second
- https://javarevisited.blogspot.com/2017/03/difference-between-first-and-second-level-cache-in-Hibernate.html


Check the first request trace:

**First Request**
```shell
curl -k 'http://localhost:8080/article/1'
```

*Duration:213.689ms Services:2 Depth:2 Total Spans:15*


Flow:

|Key|Value|
|----|----|
|http.method|GET|
|http.path|/article/1|
|mvc.controller.class|ArticleCommentController|
|mvc.controller.method|getArticle|
|sql.query|select 1|
|sql.query|set session transaction read only|
|sql.query|SET autocommit=0|
|sql.query|select article0_.id as id1_0_0_, article0_.article_text as article_2_0_0_ from article article0_ where article0_.id=1|
|sql.query|commit|
|sql.query|SET autocommit=1|
|sql.query|set session transaction read write|
|sql.query|set session transaction read only|
|sql.query|SET autocommit=0|
|sql.query|commit|
|sql.query|SET autocommit=1|
|sql.query|set session transaction read write|
|sql.query|select comments0_.article_id as article_4_2_0_, comments0_.id as id1_2_0_, comments0_.id as id1_2_1_, comments0_.article_id as article_4_2_1_, comments0_.created_at as created_2_2_1_, comments0_.comment_str as comment_3_2_1_, comments0_.user_id as user_id5_2_1_ from comment comments0_ where comments0_.article_id=1|
|sql.query|select user0_.id as id1_6_0_, user0_.date_of_birth as date_of_2_6_0_, user0_.full_name as full_nam3_6_0_ from user user0_ where user0_.id=1|

![image1](https://github.com/dineshbhagat/Spring-boot-JPA-Two-Level-Cache/blob/master/images/Screenshot%202019-08-21%20at%209.16.29%20PM.png)


**Second Request** 
```shell
curl -k 'http://localhost:8080/article/1'
```

*Duration: 19.206ms Services:2 Depth:2 Total Spans:7*
**~ 10 times improvement in response time**


Flow:

|Key|Value|
|----|----|
|http.method|GET|
|http.path|/article/1|
|mvc.controller.class|ArticleCommentController|
|mvc.controller.method|getArticle|
|sql.query|select 1|
|sql.query|set session transaction read only|
|sql.query|SET autocommit=0|
|sql.query|commit|
|sql.query|SET autocommit=1|
|sql.query|set session transaction read write|

![image2](https://github.com/dineshbhagat/Spring-boot-JPA-Two-Level-Cache/blob/master/images/Screenshot%202019-08-21%20at%209.16.19%20PM.png)
