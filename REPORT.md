# Lab 1 Git Race -- Project Report

This note uses the same disclosure fields as the group-project **AI use (10%)** slice. Lab 1 is still **limited**: assistive GenAI only — not a full or substantial generated solution. The project will later expect agents plus `AGENTS.md` and one skill; you do **not** need those here.

Do not invent a percentage of “AI vs original” lines. Empty or fake disclosure fails this lab.

## What I specified

Para saber si funcionaría antes de implementarlo, tenía pensado reutilizar los tests existentes y adaptarlos según las nuevas funcionalidades, además de crear pruebas específicas para nuevas clases. Lo más importante era comprobar la funcionalidad, la respuesta de los endpoints y lo que devolvía el JSON.

Lo que finalmente modifiqué fue el endpoint `/api/hello` de la forma mencionada en `README.md`; además, creé un nuevo endpoint llamado `/api/last-greetings` para obtener los 13 saludos más recientes.

Para comprobar los cambios, creé la prueba para la clase `HelloService` para verificar que cumplía con lo esperado, además de modificar los tests anteriores tal y como se menciona en el README.

## What I changed

- `src/main/kotlin/controller/HelloController.kt`: actualización del endpoint `/api/hello` y creación del nuevo endpoint.
- `src/main/kotlin/service/HelloService.kt`: nueva clase que gestiona los saludos según la hora y el idioma, además de la funcionalidad para guardar los 13 más recientes en una cola.
- `src/main/resources/templates/welcome.html`: añadí la interfaz para mostrar los últimos saludos.
- `src/test/kotlin/service/HelloServiceUnitTest.kt`: nuevo test creado para comprobar la funcionalidad de `HelloService`.
- `src/test/kotlin/controller/HelloControllerMVCTests.kt` y `src/test/kotlin/controller/HelloControllerUnitTests.kt`: actualización de los tests antiguos de `/api/hello`.
- `src/test/kotlin/IntegrationTest.kt`: actualización del endpoint antiguo y prueba del nuevo endpoint de últimos saludos.

## Technical decisions

Añadí el parámetro opcional `hour` a la petición de `/api/hello` para poder probar los saludos de forma más fiable. Sin este parámetro, solo podía comprobar que el JSON devolvía el nombre que se le pasaba, pero no si el saludo era correcto, porque dependía de la hora local del sistema y no podía probar bien todos los rangos horarios.

Creé la clase `HelloService` como se ha mencionado anteriormente para separar la lógica de los saludos de los controladores. Los controladores se encargan de recibir las peticiones y devolver las vistas o el JSON, mientras que el servicio es el responsable de decidir qué devolver.

Para los últimos saludos, decidí usar `ArrayDeque` de Kotlin. Los nuevos saludos se añaden al inicio y se eliminan los últimos cuando se supera el máximo.

También empecé a desarrollar un juego, pero no tuve tiempo suficiente para terminarlo correctamente, así que finalmente lo descarté.

## How I verified

Primero lancé el test del servicio y del MVC controller:

```bash
./gradlew test --tests "es.unizar.webeng.hello.service.HelloServiceUnitTests" \
  --tests "es.unizar.webeng.hello.controller.HelloControllerMVCTests"
```

Al principio tuve varios fallos menores, sobre todo en `MVCTest`, debido a los cambios que había hecho en `/api/hello` y que no había contemplado en los tests antiguos. También tuve que adaptar el test de `HelloService` más tarde para tener en cuenta la hora.

También me surgieron algunos errores por importaciones entre clases o llamadas mal realizadas, pero estos se solucionaron rápidamente.

Finalmente, ejecuté todos los tests con el comando:

```bash
./gradlew check
```

## AI disclosure

- **Tools / skills:** ChatGPT y GitHub Copilot.
- **Purpose:** Utilicé ambos para terminar de entender algunas clases y ver cómo funcionaban los endpoints y la relación entre ellos, para pensar en posibles mejoras y saber cómo organizarlo. También me ayudaron a resolver errores o problemas que fueron surgiendo.
- **Representative prompts:** “Explícame cómo funciona el controlador y las llamadas que se hacen desde el frontend”, “Sugiéreme una mejora en el backend para esta práctica”, “¿Cómo organizarías la implementación de nuevos saludos y su listado?”.
- **Affected files/sections:** Prácticamente, en mayor o menor medida, los utilicé en todos los archivos modificados; en algunos solo para aclarar dudas sobre `HelloService` y sus tests, y en otros con mucha ayuda del autocompletado de Copilot, como en `welcome.html`.
- **Validation steps:** Cuando me sugería algo ChatGPT, pensaba si tendría sentido y, si lo tenía, comprobaba también haciendo más preguntas sobre qué ocurriría en distintos escenarios y entendiendo mejor el código. También, por supuesto, revisé los tests modificados y el nuevo.
- **Citations:** No utilicé código externo.
- **Human-reviewed:** Revisé las sugerencias y decidí cuáles mantener y cuáles cambiar. Para los saludos recientes, al principio pensé en utilizar un `ArrayList` al venir de Java, pero Copilot me sugirió `ArrayDeque`. Después de comprobar cómo funcionaba, me pareció más apropiado porque es una cola simple y permite mantener los saludos ordenados y eliminar fácilmente los más antiguos. Copilot también sugirió usar `synchronized` al acceder a la cola; no me pareció muy necesario para esta práctica, pero decidí incluirlo para mantener el acceso a la cola sincronizado en caso de que llegaran varias peticiones al mismo tiempo.

También me sugirió desarrollar un juego y empecé a implementarlo como mencioné antes, en otra rama del proyecto. Sin embargo, finalmente decidí no incluirlo en la versión final porque no tuve tiempo suficiente para terminarlo.
