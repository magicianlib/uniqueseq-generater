take 1 unique sequence:

```bash
$ curl \
--request POST \
http://localhost:8080/api/seq/take \
--header 'Content-Type: application/json' \
-d'{"seqname":"commons"}'
```

take 10 unique sequence:

```bash
$ curl \
--request POST \
http://localhost:8080/api/seq/take/10 \
--header 'Content-Type: application/json' \
-d'{"seqname":"commons"}'
```