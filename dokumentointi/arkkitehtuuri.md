# Arkkitehtuurikuvaus

## Rakenne

Pakkaus app.gcdb.ui sisältää JavaFX:llä toteutetun käyttöliittymän, app.gcdb.domain ohjelman sovelluslogiikan,
app.gcdb.database tietokantayhteyden hallinnan ja app.gcdb.dao tietokannanhallinnasta vastaavan koodin.

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

## Sovelluslogiikka

Sovelluksen loogisen datamallin muodostavat käyttäjä (User), jolla on pelialustoja (Platform), joilla 
taas on pelejä (Game).

![keskeinenlogiikka](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/fundamentallogic.jpg)

Sovelluslogiikan suorittamisesta vastaa GcdbService-olio. GcdbService-oliolla on pääsy kaikkiin tietokannan
sisältämiin tietoihin Dao-pakkauksen luokkien kautta. GcdbService tuntee kulloinkin sisäänkirjautuneen käyttäjän
ja kulloinkin GUI:lla valittuna olevan pelialustan (Platform). GcdbService luo kunkin sisäänkirjautumisen yhteydessä
sisäänkirjautuneelle käyttäjälle (User) omat PlatformDao ja GameDao -oliot.

Ohjelma jakautuu neljään pakkaukseen:
* app.gcdb.dao sisältää SQLITE-tietokannan hallintaa suorittavat DAO-luokat
* app.gcdb.database sisältää tietokantayhteyden hallintaa suorittavan Database-luokan
* app.gcdb.domain sisältää ohjelman logiikan kannalta keskeiset User, Game, Platform -luokat, 
ohjelmalogiikasta vastaavan GcdbService -luokan sekä GameContent- ja GameCondition -luokat.
* app.gcdb.ui sisältää käyttöliittymästä vastaavan GUI-luokan.

Ohjelman pakkauskaavio on seuraava:

![pakkauskaavio](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/classdiagram.jpg)

## Tietojen pysyväistallennus

Pakkauksen app.gcdb.dao -luokat vastaavat käyttäjien, alustojen ja pelien tallentamisesta tietokantaan. 
Tietokanta sisältää lisäksi UserPlatform -toiminnallisuuden, joka yhdistää käyttäjän pelialustoihin.

Sovellus ottaa tietokantayhteyden jokaisen tehdyn muutoksen päivittämiseksi. Ohjelman suorituksenaikaiset listat
ovat näin ollen jatkuvasti ajan tasalla. Tämän vuoksi esimerkiksi ohjelman sammuttaminen ei kumoa
viimeisiä muutoksia.

Ohjelma käyttää tietokannanhallintaan SQLITE:a Javan JDBC:n kautta. Ohjelma käyttää tietokantanaan 
gcdb.db -tiedostoa. Ohjelman testeihin käytetään testdb.db -tiedostoa, jonka taulut ovat kopio ohjelman
varsinaisesta tietokannasta.

Ohjelma ei tallenna salasanoja selväkielisenä, vaan Javan String-luokan metodin hashcode tuottamina numerosarjoina.

Gcdb.db sisältää seuraavat tietokantataulut: 
* User(id, username, passhash)
* Platform(id, name)
* Game(id, user_id, platform_id, name, condition, content, region, comment)
* UserPlatform(user_id, platform_id)

## Päätoiminnallisuudet

### Sisäänkirjautuminen

![loginsequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/loginsequence.jpg)

### Uuden käyttäjän luominen

![newusersequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/newusersequence.jpg)

### Pelialustan lisääminen

### Pelin lisääminen

### Uloskirjautuminen

## Ohjelman rakenteeseen jääneet heikkoudet
### Käyttöliittymä

Vaikka sovelluslogiikka on pyritty erottamaan käyttöliittymästä, on käyttöliittymä melko massiivinen. 
Tämän epäkohdan voisi korjata käyttämällä FXML-määrittelyä.
