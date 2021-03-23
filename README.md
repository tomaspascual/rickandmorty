# Easy usage

Just to make it easy, a couple of examples to make it work

## Endpoints

GET /api/rickandmorti

GET /api/rickandmorty?excluded=1,2,3


# To startup the environment with Docker

- mvn --batch-mode verify -P docker

- docker build  -t rickandmorty/rickandmorty:latest .

- docker run -p 8080:8080 -it rickandmorty/rickandmorty:latest