FROM eclipse-temurin:17

ENV APP_USER=rating-bot
ENV PIPE=/opt/rating_pipe
ENV APP_FOLDER=/app

RUN useradd -ms /bin/bash $APP_USER \
    && mkdir $APP_FOLDER \
    && chown -R $APP_USER /app \
    && mkfifo $PIPE \
    && chown $APP_USER $PIPE

USER $APP_USER

COPY ./target/rating-bot.jar /app/rating-bot.jar
WORKDIR $APP_FOLDER

CMD ["java", "-jar", "rating-bot.jar"]
