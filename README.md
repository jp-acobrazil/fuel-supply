# Supply Management API

API para gerenciamento de abastecimentos de veículos.

## Tecnologias

- Java 21
- Spring Boot 3.x
- Lombok
- Maven/Gradle (especifique seu build)

## Endpoints Principais

- `POST /api/supplies`  
  Adiciona um novo abastecimento.  
  - Corpo da requisição (`JSON`):
    ```json
    {
      "driverId": 123,
      "liters": 50.5,
      "pricePerLiter": 5.49,
      "plate": "ABC1234",
      "fuelType": "Diesel",
      "odometer": 12000,
      "stationCnpj": "12345678000190",
      "obs": "Abastecimento no posto X"
    }
    ```

- Outros endpoints serão adicionados conforme o projeto evolui.
