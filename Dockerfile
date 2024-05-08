FROM eclipse-temurin:17

ENV APPLICATION_USER rating-bot
RUN useradd -ms /bin/bash $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./target/scala-3.3.1/rating-bot.jar /app/rating-bot.jar
WORKDIR /app

CMD ["java", "-jar", "rating-bot.jar"]
