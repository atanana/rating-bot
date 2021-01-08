FROM openjdk:11

ENV APPLICATION_USER rating-bot
RUN useradd -ms /bin/bash $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./target/scala-2.13/rating-bot.jar /app/rating-bot.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UseContainerSupport", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "rating-bot.jar"]