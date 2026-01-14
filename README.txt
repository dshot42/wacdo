Développeur : DUBOST Côme
Soutenance Date : 16/01/2026

Projet Wacdo :
Développement du logiciel de gestion des affectations
des collaborateurs dans les différents restaurants de Wacdo

Stack technique :

    Back End :
    *   Gradle 9.0
    *   Java 17
    *   Java Spring boot 3.5.7
    *   SGBD : PostgreSQL
    *   Test : SpringBootTest (coverage) # BDD test : H2

    Front end :
    *   Thymeleaf
    *   JS : vanilla natif
    *   CSS : natif, bootstrap


Local :
*   port 8000
*   Variable d'envirronement : SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/wacdo

Production:
*   Distrubution via Koyeb.com : https://suspicious-hippopotamus-dubost-e083975d.koyeb.app/login
*   Variable d'envirronement : SPRING_DATASOURCE_URL=jdbc:postgresql://ep-calm-resonance-agtaewc5.c-2.eu-central-1.pg.koyeb.app/koyebdb?user=koyeb-adm&password=npg_Hg8LqRmNpn6t

GitHub :
*    Repository : https://github.com/dshot42/wacdo

Route utils:
*   /login :  default access {id : superAdmin, pwd : 123}
*   /test/insertPlanningGame : reset data and insert game plan


Command Launcher :
*   Test: gradle :test
*   Run Developpement : gradle run // ( $env:VAR="value"; gradle bootRun )
)
*   Run Distribution : gradle bootRun // ( $env:VAR="value"; gradle bootRun )