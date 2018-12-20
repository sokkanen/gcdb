# Arkkitehtuurikuvaus

## Rakenne

Pakkaus ![app.gcdb.ui](https://github.com/sokkanen/ot-harjoitustyo/tree/master/src/main/java/app/gcdb/ui) sisältää JavaFX:llä toteutetun käyttöliittymän, ![app.gcdb.domain](https://github.com/sokkanen/ot-harjoitustyo/tree/master/src/main/java/app/gcdb/domain) ohjelman sovelluslogiikan,
![app.gcdb.database](https://github.com/sokkanen/ot-harjoitustyo/tree/master/src/main/java/app/gcdb/database) tietokantayhteyden hallinnan ja app.gcdb.dao tietokannanhallinnasta vastaavan koodin.

## Käyttöliittymä

Käyttöliittymä sisältää neljä näkymää:
* Kirjautuminen
* Uuden käyttäjän luominen
* Päänäkymä (Pelien ja Alustojen hallinnointi)
* Ohjelman sammutusnäkymä

Kirjautuminen, Uuden käyttäjän luominen ja Päänäkymä on toteutettu erillisinä [Scene]:https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html -olioina, joista yksi
kerrallaan on sijoitettuna [Stage]:http://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html -olioon. Sammutusnäkymä on toteutettu Kirjautumisnäkymän pohjalla.

Käyttöliittymä on pyritty eristämään sovelluslogiikasta. Käyttöliittymäluokka ![GUI](https://github.com/sokkanen/ot-harjoitustyo/blob/master/src/main/java/app/gcdb/ui/GUI.java) käyttää sovelluslogiikan
suorittamiseen ![GcdbService](http://github.com/sokkanen/ot-harjoitustyo/blob/master/src/main/java/app/gcdb/domain/GcdbService.java)-luokan metodeja.

## Sovelluslogiikka

Sovelluksen loogisen datamallin muodostavat käyttäjä (![User](https://github.com/sokkanen/ot-harjoitustyo/blob/master/src/main/java/app/gcdb/domain/User.java)), jolla on pelialustoja (![Platform](https://github.com/sokkanen/ot-harjoitustyo/blob/master/src/main/java/app/gcdb/domain/Platform.java)), joilla 
taas on pelejä (![Game](https://github.com/sokkanen/ot-harjoitustyo/blob/master/src/main/java/app/gcdb/domain/Game.java)).

![keskeinenlogiikka](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/fundamentallogic.jpg)

Sovelluslogiikan suorittamisesta vastaa GcdbService-olio. GcdbService-oliolla on pääsy kaikkiin tietokannan
sisältämiin tietoihin Dao-pakkauksen luokkien kautta. GcdbService tuntee kulloinkin sisäänkirjautuneen käyttäjän
ja kulloinkin GUI:lla valittuna olevan pelialustan (Platform). GcdbService luo kunkin sisäänkirjautumisen yhteydessä
sisäänkirjautuneelle käyttäjälle (User) omat PlatformDao ja GameDao -oliot. PlatformDao käyttää UserPlatform-oliota
käyttäjään liittyvän alustojen tietokantahallinnan toteuttamiseen.

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

Ohjelma käyttää tietokannanhallintaan SQLITE:a Javan JDBC:n kautta. Ohjelma käyttää oletustietokantanaan 
gcdb.db -tiedostoa. Ohjelman testeihin käytetään testdb.db -tiedostoa, jonka taulut ovat kopio ohjelman
varsinaisesta tietokannasta.

Käyttäjä voi määrittää haluamansa tietokantaosoitteen ohjelmahakemiston juuresta löytyvää gcdb.conf -tiedostoa
muokkaamalla.

Ohjelma ei tallenna salasanoja selväkielisenä, vaan Javan String-luokan metodin hashcode tuottamina numerosarjoina.

Gcdb.db (tai käyttäjän määrittämä tietokanta) sisältää seuraavat tietokantataulut: 
* User(id, username, passhash)
* Platform(id, name)
* Game(id, user_id, platform_id, name, condition, content, region, comment)
* UserPlatform(user_id, platform_id)

### Tiedostot

Sovellus olettaa, että sovellustiedoston hakemiston juuresta löytyy gcdb.conf-tiedosto, joka määrittelee
käytettävän tietokannan nimen. Oletuksena ohjelma käyttää valmiiksi rakennettua, JAR-pakkaukseen pakattua 
gcdb.db-tietokantatiedostoa. Käyttäjä voi halutessaan valita tietokannalle oletuksesta poikkeavan hakemiston ja nimen. 

## Päätoiminnallisuudet

### Sisäänkirjautuminen

Kun kirjautumisnäkymään on syötetty käyttäjätunnus ja salasana, ja salasanakentässä painetaan <ENTER>, etenee
sovelluksen kontrolli seuraavasti:

![loginsequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/loginsequence.jpg)

Sisäänkirjautumisen yhteydessä selvitetään onko käyttäjätunnus olemassa ja täsmääkö annetun salasanan tiiviste
tietokannasta löytyvään tiivisteeseen. Jos käyttäjätunnus ja salasana täsmäävät, antaa ohjelma vaihtoehdoiksi
jatkaa ohjelmaan tai kirjautua ulos. Ohjelmaan jatkettaessa ajetaan GUI-luokassa loadMainWindow()-metodi.

### Uuden käyttäjän luominen

Ohjelma tarkastaa syötetyn käyttäjätunnuksen ja salasanan pituusvaatimukset, sekä varmistaa, ettei samalla 
käyttäjätunnuksella ole käyttäjää tietokannassa.

![newusersequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/newusersequence.jpg)

Näkymä ei automaattisesti palaa kirjautumisnäkymään, vaan samalla käyttäjän lisäämisellä voi luoda useita
käyttäjätunnuksia. Paluu kirjautumisnäkymään tapahtuu "Back"-painiketta painamalla.

### Pelialustan lisääminen

Kun uuden pelialustan nimi on syötetty nimikenttään ja käyttäjä painaa "Add a platform" -painiketta, 
tarkastetaan, ettei pelialustan nimi ole tyhjä ja ettei tietokannassa ole jo kyseiselle käyttäjälle lisätty
nyt lisättävää pelialustaa. Tämän jälkeen ohjelman kontrolli etenee seuraavasti:

![newpltfrmsequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/newpltfrmsequence.jpg)

Pelialusta lisätään tietokantaan Platform-tauluun. UserPlatform-tauluun luodaan viite käyttäjän ja alustan
id-numeroiden perusteella. Päivitetty pelialustalista lisätään User-oliolle, jolta haetaan GUI:n ListView-oliolle
platformList näytettäväksi lista käyttäjän pelialustoista.

### Pelin lisääminen

Ohjelma tarkastaa, että uudelle pelille on annettu nimi, kuntoluokka ja sisältö. Lisäksi tarkastetaan, ettei 
toista täysin samansisältöistä peliä ole jo listalla. Useammat samat pelit käyttäjä voi erotella esimerkiksi 
kommentein. Kun ehdot täyttyvät ja käyttäjä painaa "Add a game"-painiketta, etenee ohjelman kontrolli seuraavasti:

![newgamesequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/newgamesequence.jpg)

Peli lisätään tietokantaan Game-tauluun. Päivitetty pelilista lisätään User-oliolle, jolta haetaan GUI:n 
ListView-oliolle gameList näytettäväksi lista käyttäjän peleistä.

### Pelin poistaminen

Kun käyttäjä on valinnut pelin hiiren painikkeella ja painaa "Remove selected game"-painiketta, etenee ohjelman
kontrolli seuraavasti: 

![deletegamesequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/deletegamesequence.jpg)

Peli poistetaan tietokannan Game-taulusta. Päivitetty pelilista lisätään User-oliolle, jolta haetaan GUI:n 
ListView-oliolle gameList näytettäväksi lista käyttäjän peleistä.

### Pelialustan poistaminen

Kun käyttäjä on valinnut alustan hiiren painikkeella ja painaa "Remove selected platform"-painiketta, etenee ohjelman
kontrolli seuraavasti:

![deletepltfrmsequence](https://github.com/sokkanen/ot-harjoitustyo/blob/master/dokumentointi/kuvat/deletepltfrmsequence.jpg)

Alustan viite poistetaan tietokannan UserPlatform-taulusta. PlatformDao tarkastaa UserPlatformDao:lta, onko
poistettava pelialusta listattuna jollakin käyttäjällä. Mikäli pelialustaa ei ole yhdelläkään käyttäjällä, poistaa
PlatformDao alustan myös Platform-taulusta. Päivitetty alustalista lisätään User-oliolle, jolta haetaan GUI:n
ListView-oliolle PlatformList näytettäväksi lista käyttäjän alustoista.

### Pelin muokkaaminen

Pelin muokkaaminen on toteutettu käyttämällä hyväksi valmiita pelin poistamisen ja lisäämisen toiminnallisuuksia.
Pelin muokkaaminen siis poistaa pelin ja lisää uuden pelin päivitetyillä tiedoilla.
Pelin muokkaamisessa on mahdollista muokata pelin kuntoluokkaa, sisältöä, aluekoodia ja kommenttia.

## Ohjelman rakenteeseen jääneet heikkoudet

### Käyttöliittymä

Vaikka sovelluslogiikka on pyritty erottamaan käyttöliittymästä, on käyttöliittymä melko massiivinen. 
Tämän epäkohdan voisi korjata käyttämällä FXML-määrittelyä.

### Dao-luokat

DAO-luokilla on käyttämättömiä boolean-tyyppisiä palautusarvoja, jotka olisi voinut jättää tästä ohjelman versiosta
pois. Palautusarvot säilytettiin, jotta tulevan ohjelman laajentaminen olisi vaivattomampaa (mm. tarkempi palaute
käyttäjälle.).
