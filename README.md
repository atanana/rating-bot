# rating-bot

[![Build Status](https://travis-ci.org/atanana/rating-bot.svg?branch=master)](https://travis-ci.org/atanana/rating-bot) ![Tests](https://github.com/atanana/rating-bot/workflows/Tests/badge.svg)

Следит за командой и спамит чат в телеграме мотивирующими сообщениями

Настройки в config.json:

    {
      "token": "test", // токен для vk апи
      "chat": 1, // идентификатор беседы
      "team": 2, // идентификатор команды
      "city": 3, // идентификатор города
      "cityName": "Минск", // название города
      "countryName": "Беларусь", // название страны
      "port": 11000 // порт для отправки сообщений
    }