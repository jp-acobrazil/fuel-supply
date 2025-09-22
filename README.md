# Supply Management API

API para gerenciamento de abastecimentos de veículos.

## Tecnologias

- Java 21
- Spring Boot 3.x
- Lombok
- Maven/Gradle (especifique seu build)

## Endpoints da API

### Adicionar um novo abastecimento

**POST** `/api/supplies`

- **Descrição:** Cria um novo registro de abastecimento.
- **Request Body (JSON):**

```json
{
  "id": 5,
  "date": "2025-09-22T14:35:21",
  "liters": 20,
  "pricePerLiter": 5.59,
  "fuelType": "Diesel",
  "odometer": 12000,
  "obs": null,
  "driver": {
      "driverId": 98,
      "name": "MICHAEL DE OLIVEIRA DANTAS"
  },
  "vehicle": {
      "vehicleId": 34,
      "plate": "NNK6541",
      "carType": "P",
      "description": "1001 - IVECO TECTOR 240E25",
      "isOwn": "N"
  }
}
```

**GET** `/api/supplies`

- **Descrição:** Retorna todos os abastecimentos cadastrados.
- **Response (JSON):**
  
```json
[
  {
    "id": 4,
    "date": "2025-09-22T14:31:44",
    "liters": 50.5,
    "pricePerLiter": 5.49,
    "fuelType": "Diesel",
    "odometer": 12000,
    "obs": null,
    "driver": {
        "driverId": 98,
        "name": "MICHAEL DE OLIVEIRA DANTAS"
    },
    "vehicle": {
        "vehicleId": 34,
        "plate": "NNK6541",
        "carType": "P",
        "description": "1001 - IVECO TECTOR 240E25",
        "isOwn": "N"
    }
  },
  {
    "id": 5,
    "date": "2025-09-22T14:35:21",
    "liters": 20,
    "pricePerLiter": 5.59,
    "fuelType": "Diesel",
    "odometer": 12000,
    "obs": null,
    "driver": {
        "driverId": 98,
        "name": "MICHAEL DE OLIVEIRA DANTAS"
    },
    "vehicle": {
        "vehicleId": 34,
        "plate": "NNK6541",
        "carType": "P",
        "description": "1001 - IVECO TECTOR 240E25",
        "isOwn": "N"
    }
  }
]
```

**GET** `/api/supplies/{id}`
- **Descrição:** Retorna um abastecimento específico pelo seu ID.
- **PathVariable: id → ID do abastecimento.**
- **Response (JSON):**
```json
{
  "id": 5,
  "date": "2025-09-22T14:35:21",
  "liters": 20,
  "pricePerLiter": 5.59,
  "fuelType": "Diesel",
  "odometer": 12000,
  "obs": null,
  "driver": {
      "driverId": 98,
      "name": "MICHAEL DE OLIVEIRA DANTAS"
  },
  "vehicle": {
      "vehicleId": 34,
      "plate": "NNK6541",
      "carType": "P",
      "description": "1001 - IVECO TECTOR 240E25",
      "isOwn": "N"
  }
}
```
