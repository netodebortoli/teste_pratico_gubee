<h1 align="center">
  Teste prático Gubee - API Heroes
</h1>

API desenvolvida em Java, que oferece gerenciamento de hérois (CRUD) e comparação entre hérois (quem é o mais forte/fraco), como desafio proposto para vaga de estágio pela Gubee.

## Tecnologias e Ferramentas

- Spring Boot
- Spring JDBC
- PostgresSQL
- Flyway
- Docker

## Práticas adotadas

- API Rest
- Consultas e consultas com filtro
- Paginação
- Uso de DTOs e Mapper
- Injeção de Dependências
- Auditoria sobre criação e atualização da entidade
- Tratamento de erro e exceção personalizada

## Executando a aplicação
### _Pelo Docker_
- Ligar o serviço do Docker
- Clonar este repositório
- Abrir a pasta principal(raíz) do projeto
- Executar o comando que irá subir a API e o Banco de Dados em dois containers:
````
docker compose up
````
### _Localmente_
- Ligar o serviço do Docker
- Clonar este repositório
- Abrir a pasta principal(raíz) do projeto
- Executar o comando que irá subir um container do Banco de Dados
```
docker compose up db
```
- Construir o projeto:
```
mvn clean package
```
- Executar o **_.jar_** do projeto: 
```
java -jar core/target/gubee-interview.jar
```
## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [Insomnia](https://insomnia.rest/download)

A API é acessada localmente em:
````
localhost:8080/api/v1/heroes
````
- POST
```
Request: localhost:8080/api/v1/heroes
Content-Type: application/json
{
	"name": "LANTERNA VERDE",
	"race": "HUMAN",
	"strength": 8,
	"intelligence": 8,
	"dexterity": 10,
	"agility": 7
}

Response:
Status: 201 CREATED
```
- PUT /{id}
```
Request: http://localhost:8080/api/v1/heroes/{id}
Content-Type: application/json
{
	"name": "SUPERMAN",
	"race": "HUMAN",
	"strength": 10,
	"intelligence": 9,
	"dexterity": 10,
	"agility": 10
}

Response:
Status: 200 OK
{
	"name": "SUPERMAN",
	"race": "HUMAN",
	"strength": 10,
	"intelligence": 9,
	"dexterity": 10,
	"agility": 10
}
```
- DELETE /{id}
```
Request: http://localhost:8080/api/v1/heroes/{id}

Response:
Status: 204 NO CONTENT
```
- GET
```
Request: localhost:8080/api/v1/heroes

Response:
Status: 200 OK
{
	"result": [],
	"total_pages": 0,
	"total_elements": 0,
	"current_page": 0
}
```
- GET /{id}
```
Request: localhost:8080/api/v1/heroes/{id}
Content-Type: application/json

Response:
Status: 200 OK
{
	"name": "FLASH",
	"race": "HUMAN",
	"strength": 5,
	"agility": 10,
	"dexterity": 6,
	"intelligence": 7
}
```

- GET ?name=exemplo
```
Request: http://localhost:8080/api/v1/heroes?name=mulher
Content-Type: application/json

Response:
Status: 200 OK
{
	"result": [
		{
			"name": "MULHER MARAVILHA",
			"race": "DIVINE",
			"strength": 10,
			"agility": 7,
			"dexterity": 10,
			"intelligence": 5
		}
	],
	"total_pages": 1,
	"total_elements": 1,
	"current_page": 0
}
```

- GET ?page=5&pageSize=1
```
Request: http://localhost:8080/api/v1/heroes?page=0&pageSize=5
Content-Type: application/json

Response:
Status: 200 OK
{
	"result": [
		{
			"name": "FLASH",
			"race": "HUMAN",
			"strength": 10,
			"agility": 7,
			"dexterity": 10,
			"intelligence": 5
		}
	],
	"total_pages": 11,
	"total_elements": 11,
	"current_page": 5
}
```

- GET ?page=0&pageSize=5&name=batman
```
Request: http://localhost:8080/api/v1/heroes?page=0&pageSize=5&name=batman
Content-Type: application/json

Response:
Status: 200 OK
{
	"result": [
		{
			"name": "BATMAN",
			"race": "HUMAN",
			"strength": 10,
			"agility": 7,
			"dexterity": 10,
			"intelligence": 5
		}
	],
	"total_pages": 1,
	"total_elements": 1,
	"current_page": 0
}
```

- GET /compare
```
Request: http://localhost:8080/api/v1/heroes/compare?heroOneId={id}&heroTwoId={id}

Response:
Status: 200 OK
{
	"hero_one_id": "{id}",
	"power_stats_hero_one": {
		"strength": "10 (+8)",
		"agility": "7 (+5)",
		"dexterity": "10 (+8)",
		"intelligence": "5 (-5)"
	},
	"hero_two_id": "{id}",
	"power_stats_hero_two": {
		"strength": "2 (-8)",
		"agility": "2 (-5)",
		"dexterity": "2 (-8)",
		"intelligence": "10 (+5)"
	}
}
```
