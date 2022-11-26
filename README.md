# API-Desafio

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=coverage)](https://sonarcloud.io/component_measures?id=AntonioSgarbi_desafio-api-GFT&metric=coverage)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=ncloc)](https://sonarcloud.io/dashboard?id=AntonioSgarbi_desafio-api-GFT)
[<img src="https://img.shields.io/badge/dockerhub-image-success.svg?logo=docker">](https://hub.docker.com/r/antoniosk/desafio-api)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)
[![Sqale_Index](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=bugs)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AntonioSgarbi_desafio-api-GFT&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AntonioSgarbi_desafio-api-GFT)

## Descrição

### Middleware para consumo de notícias. 

## Proposta
```
Neste desafio você irá desenvolver um middleware para usuários consumirem
notícias de forma rápida e prática através do consumo da API de Notícias
Gratuitas (https://apinoticias.tedk.com.br/).

O sistema deverá conter endpoints para as seguintes funcionalidades:

- Cadastro de administrador (somente adm)
- Cadastro de usuário (somente adm)
- Cadastro de etiqueta para usuário (somente usuário)
- Histórico de parâmetros acessados (acessado por adm e o próprio usuário)
- Histórico de etiquetas mais acessadas (somente adm)
- Acesso às notícias com as etiquetas cadastradas (para o usuário. Caso
  não haja etiqueta cadastrada, deverá retornar um erro com a descrição
  correspondente)
- Popular banco

Exceeds:
- Testes unitários
- JWT e/ou Refresh Token
- Swagger ou Projeto no Postman ou OpenAPI
- Endpoint para envio de e-mail com notícias da data corrente para
  usuários de acordo com suas etiquetas (somente adm)
- Envio de e-mail para usuário cadastrado no sistema no ato do cadastro 
```

## Sobre

### Este projeto implementa todas as Funcionalidades e Exceeds propostos 

## Play

```
docker-compose up
```
#### (consumo preparado via Swagger no caminho '/swagger-ui/index.html')
