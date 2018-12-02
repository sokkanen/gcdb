# Arkkitehtuurikuvaus

## Rakenne

Ohjelman luokkakaavio on seuraava:

![luokkakaavio](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/arkkitehtuuri.jpg)

Ohjelma jakautuu neljään pakkaukseen:
* app.gcdb.dao sisältää SQLITE-tietokannan hallintaa suorittavat DAO-luokat
* app.gcdb.database sisältää tietokantayhteyden hallintaa suorittavan Database-luokan
* app.gcdb.domain sisältää ohjelman logiikan kannalta keskeiset User, Game, Platform -luokat 
sekä ohjelmalogiikasta vastaavan GcdbService -luokan.
* app.gcdb.ui sisältää käyttöliittymästä vastaavan GUI-luokan.

## Käyttöliittymä

Käyttöliittymä sisältää neljä näkymää:
* Kirjautuminen
* Uuden käyttäjän luominen
* Päänäkymä (Pelien ja Alustojen hallinnointi)
* Ohjelman sammutusnäkymä

Kirjautuminen, Uuden käyttäjän luominen ja Päänäkymä on toteutettu erillisinä Scene-olioina, joista yksi
kerrallaan on sijoitettuna Stage-olioon. Sammutusnäkymä on toteutettu Kirjautumisnäkymän pohjalla.

Käyttöliittymä on pyritty eristämään sovelluslogiikasta. Käyttöliittymäluokka GUI käyttää sovelluslogiikan
suorittamiseen GcdbService-luokan metodeja.

Sovellus ottaa tietokantayhteyden jokaisen tehdyn muutoksen päivittämiseksi. Ohjelman sisäiset listat ovat
näin ollen jatkuvasti ajantasalla. Näin ollen esimerkiksi ohjelman sammuttaminen ei kumoa viimeisiä muutoksia.

## Tietojen pysyväistallennus

Pakkauksen app.gcdb.dao -luokat vastaavat käyttäjien, alustojen ja pelien tallentamisesta tietokantaan. 
Tietokanta sisältää lisäksi UserPlatform -toiminnallisuuden, joka yhdistää käyttäjän pelialustoihin.

Ohjelma käyttää tietokannanhallintaan SQLITE:a Javan JDBC:n kautta. Ohjelma käyttää tietokantanaan 
gcdb.db -tiedostoa. Ohjelman testeihin käytetään testdb.db -tiedostoa, jonka taulut ovat kopio ohjelman
varsinaisesta tietokannasta.

Ohjelma ei tallenna salasanoja selväkielisenä, vaan Javan String-luokan metodin hashcode tuottamina numerosarjoina.

Gcdb.db sisältää seuraavat tietokantataulut: 
* User(id, username, passhash)
* Platform(id, name)
* Game(id, user_id, platform_id, name, condition, content, comment)
* UserPlatform(user_id, platform_id)

## Päätoiminnallisuudet

### Sisäänkirjautuminen

![loginsequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/loginsequence.jpg)

### Uuden käyttäjän luominen

### Pelialustan lisääminen

### Pelin lisääminen

### Uloskirjautuminen

## Ohjelman rakenteeseen jääneet heikkoudet
### Käyttöliittymä

Vaikka sovelluslogiikka on pyritty erottamaan käyttöliittymästä, on käyttöliittymä melko massiivinen. 
Tämän epäkohdan voisi korjata käyttämällä FXML-määrittelyä.
