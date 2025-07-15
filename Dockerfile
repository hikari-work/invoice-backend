
FROM openjdk:21-jdk-alpine


WORKDIR /app

COPY target/IntegerasiOrderKuota-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "app.jar"]

CMD [
  "--server.port=9999",
  "--spring.datasource.url=jdbc:mysql://db-mysql-sgp1-80521-do-user-11365462-0.l.db.ondigitalocean.com:25060/cek",
  "--spring.datasource.username=doadmin",
  "--spring.datasource.password=AVNS_RykB805c6Z2OUGXFLOy",
  "--spring.datasource.hikari.minimum-idle=2",
  "--spring.datasource.hikari.maximum-pool-size=100",
  "--spring.jpa.properties.hibernate.show_sql=false"
]
