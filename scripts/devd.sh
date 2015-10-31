dir=rest-server/src/main/resources/assets
devd -l -p 8000 \
    -w ./$dir \
    /service/=http://localhost:8080/service/ \
    /=./$dir

