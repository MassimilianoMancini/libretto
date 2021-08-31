# Libretto
Questo progetto è realizzato nell'ambito del corso di laurea magistrale di Informatica dell'Università di Firenze, Advanced Programming Techniques. Scopo principale del progetto è mostrare l'utilizzo corretto delle tecniche avanzate di programmazione studiate durante le lezioni. Il progetto realizza la semplice gestione di un libretto universitario con operazioni CRUD sugli esami sostenuti con successo nonché il calcolo della media aritmetica e la media ponderata.

## Tecniche di programmazione utilizzate
Sono utilizzate le seguenti tecniche:
- utilizzo di maven per la strutturazione del progetto, compreso l'utilizzo di moduli diversi
- utilizzo di maven per la risoluzione delle dipendenze
- sviluppo tramite metodo Test-Driven Development
- utilizzo di Google Guice per Dependency Injection
- utilizzo di mock per mantenere l'isolamento in fase di test
- utilizzo di docker per la disponibilità di database
- dockerizzazione dell'applicazione
- utilizzo di git come strumento di controllo della versione
- utilizzo di github come strumento di condivisione e centralizzazione dei codici sorgenti
- ciclo di sviluppo guidato da issue e pull request con conseguente utilizzo di differenti branch, in particolare main e develop
- verifica del code coverage sia localmente che tramite coveralls.io
- controllo mutanti e loro sopravvivenza tramite PIT
- Continous Integration su almeno due versioni java (11 e 15) e 3 diversi sistemi operativi (linux, windows, macos)
- Code Quality tramite SonarCloud

## Architettura
Il progetto viene realizzatato tramite architettura MVC

## Database
MariaDB

## Traccia per lo sviluppo
### Model
**Exam**
- ID
- description
- weight
- grade (voto?)
- date

**Libretto**
- List \<Exam\>
- getMean()
- getWeightedMean()

**Repository**
- Interface Respository
- class MariaDBRepository


### Controller
LibrettoController: 
- addExam(exam)
- deleteExam(id)
- updateExam(exam)
- getMean()
- getWeightedMean()
- findById(id)
- findAll()

### View
- interface View
- class SwingView
