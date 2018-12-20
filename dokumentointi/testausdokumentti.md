
Testausdokumentti
---------------------
Ohjelmaa on testattu niin JUnit:n tarjoamin yksikkö- ja integraatiotestein, kuin myös manuaalisilla järjestelmätesteillä.
Ohjelma käyttää testeihin erillistä testdb-tietokantaa, joka on kopio ohjelman varsinaisesta tietokannasta.

### Yksikkö ja integraatiotestaus  ###

Keskeisin testausluokka on GcdbServiceTest, joka testaa itsensä lisäksi välillisesti myös sovelluslogiikan ja Dao-luokkien toimivuutta sekä
näiden integraatiota.

#### Dao-luokat ####

Dao-luokille on omat testiluokkansa GameDaoTest, PlatformDaoTest ja UserDaoTest. PlatformDaoTest testaa myös UserPlatformDao-luokkaa, sillä
em. luokka toimii alisteisena PlaformDao:lle.

#### Sovelluslogiikka ####

GcdbServiceTest:n lisäksi sovelluslogiikan yksikkötestausta suorittavat GameTest ja UserTest-luokat.

#### Testauskattavuus ####

Käyttöliittymää lukuunottamatta testien rivikattavuus on 85% ja haaraumakattavuus 69%. Testien ulkopuoliset rivit ovat pitkälti
yksinkertaisia get- ja set-metodeita. Haaraumakattavuuden prosentuaalista arvoa laskee useiden luokkien poikkeusten (Pääsääntöisesti
SQLException) testaamattomuus.

![jacocoreport](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/jacocoresult.jpg)

### Järjestelmätestaus ###

#### Asennus ja konfigurointi ####

Ohjelman järjestelmätestaus on suoritettu manuaalisesti. Ohjelmaa on testattu sekä MacOS 10.14 Mojave ,että Ubuntu 18.04 -ympäristöissä.
Ohjelmaa on testattu sekä JAR:n sisäänpaketoidulla gcdb.db-tietokannalla, että käyttäjän erikseen määrittämällä tietokantanimellä. Molemmissa
tapauksissa järjestelmä on toiminut identtisesti.

#### Toiminnallisuudet ####

Kaikki määrittelydokumentissa ja käyttöohjeessa mainitut toiminnallisuudet on testattu. Toiminnallisuuksien
testaamisen yhteydessä myös virheellisten syötteiden antaminen on testattu.

### Virheilmoitukset ###

Järjestelmä kaatuu virheilmoitukseen, mikäli ohjelman hakemiston juuresta ei löydy gcdb.conf -tiedostoa.

