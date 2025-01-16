# Метеостанция (серверная часть)
### Проект основан на ktor server.
#### Разработан для сбора данных, передаваемых контроллерами с климатических датчиков. <br> Является мостом между контроллерами и клиентской частью с визуальным представлением данных.
#### Контроллерную (клиентскую) часть этого проекта можно найти здесь: [weather-station-controllers](https://github.com/ArtemBotnev/weather-station-controllers)

Основные функции: сбор текущих значений температуры, влажности и т. д. Эти значения отправляются контроллерами REST методом POST.
```/send_measurement```. Все значения хранятся (только!) в кэш-памяти и перезаписываются с каждой новой порцией данных от определенного контроллера с определенным набором датчиков. Каждый контроллер и датчик должны иметь уникальный идентификатор. В противном случае может возникнуть коллизия — данные от разных датчиков будут перезаписывать друг друга.
<br>
Средние, максимальные и минимальные значения за день также рассчитываются для каждого измерения с отфильтровыванием значений, которые являются ошибками датчика. Данные об ошибках датчика предоставляются контроллером. Когда в 00:00 начинается новый день, данные очищаются, начинается сбор данных и расчеты для нового дня. Данные сохраняются в кэш-памяти и будут потеряны при перезапуске приложения.
#### Развёртывание
Выполнить ```bash java -jar weather-station.jar```
<br>
Возможно установить хост и порт, где -h хост, -p порт, пример:
<br>
Выполнить ```bash java -jar weather-station.jar -h 127.0.0.1 -p 9090```
<br>
По умолчанию хост 0.0.0.0, порт - 8080
#### Основные методы REST API:
POST ```/send_measurement``` получает данные, сгенерированные контроллером.
<br>
```json
{
  "timeZoneHours": 3,
  "device": {
    "id": 0,
    "type": "Controller",
    "name": "Test controller",
    "location": "My sweet home"
  },
  "measures": [
    {
      "sensorId": "Sensor_0",
      "sensorName": "cool sensor",
      "measureName": "temperature",
      "measureValue": 29.23,
      "measureUnit": "C",
      "sensorPlace": "Just place",
      "sensorError": false
    }
  ]
}
```
POST ```/clear_cache``` техническая команда для очистки всех данных.
<br>
GET ```/devices``` описание всех контроллеров, которые являются клиентами этого сервера.
```json
[
  {
    "id": 0,
    "type": "ESP-32",
    "name": "ESP-32 id_0",
    "location": "My sweet home"
  }
]
```
GET ```/last_measurement/{device_id}``` текущие значения со всех датчиков контроллера с определенным device_id.
```json
{
  "timestamp": "2024-12-30T20:18:33.583+03:00",
  "timeZoneHours": 3,
  "device": {
    "id": 0,
    "type": "ESP-32",
    "name": "ESP-32 id_0",
    "location": "My sweet home"
  },
  "measures": [
    {
      "sensorId": "sht-45_temp",
      "sensorName": "sht-45",
      "sensorPlace": "outdoor",
      "measureName": "temperature",
      "measureValue": 0.07,
      "measureUnit": "℃"
    },
    {
      "sensorId": "sht-45_hum",
      "sensorName": "sht-45",
      "sensorPlace": "outdoor",
      "measureName": "humidity",
      "measureValue": 88.99,
      "measureUnit": "%"
    },
    {
      "sensorId": "bme_0_temp",
      "sensorName": "bme_0",
      "sensorPlace": "room",
      "measureName": "temperature",
      "measureValue": 26.76,
      "measureUnit": "℃"
    },
    {
      "sensorId": "bme_0_press",
      "sensorName": "bme_0",
      "sensorPlace": "room",
      "measureName": "atm. pressure",
      "measureValue": 739.85,
      "measureUnit": "mmHg"
    }
  ]
}
```
С query параметром ```additional=true``` получить ответ с вычисленными значениями.
```/last_measurement/0?additional=true```
```json
{
  "timestamp": "2024-12-30T20:18:33.583+03:00",
  "timeZoneHours": 3,
  "device": {
    "id": 0,
    "type": "ESP-32",
    "name": "ESP-32 id_0",
    "location": "My sweet home"
  },
  "measures": [
    {
      "sensorId": "sht-45_temp",
      "sensorName": "sht-45",
      "sensorPlace": "outdoor",
      "measureName": "temperature",
      "measureValue": 0.0,
      "measureUnit": "℃",
      "dailyCalculation": {
        "maxValue": 0.62,
        "minValue": -0.21,
        "averageValue": 0.191,
        "maxValueTime": "2024-12-30T05:36:02.533+03:00",
        "minValueTime": "2024-12-30T16:18:21.149+03:00"
      }
    }
  ]
}
```
GET ```/device_analytics``` возвращает статистику ошибок для всех датчиков всех контроллеров.

GET ```/device_analytics/{device_id}``` возвращает статистику ошибок для конкретного контоллера с {device_id}.