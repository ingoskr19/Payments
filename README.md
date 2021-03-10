Evertec test implementation - Payments

###### Project architecture  ######

- Language: Kotlin

Android JetPack:
  - ViewModel(LiveData)
  - Retrofit
  - Room Database
  - Koin ( Dependency Injection)
  - Navigation component
  - Safe Args

- Coroutines (async task)

Directories structure:
  - common (Classes used in entire app)
  - util (Utilities classes - kotlin extensions)
  - di (Koin Config - Dependency injection)
  - repository (Common classes repository)
  - screens (Bussines Modules)
    - payments
      - model (Data classes)
      - usecases (Use cases/Bussines logic layer)
      - view
          - adapter
          - fragment
          - listener
      - viewmodel


Notes: 
- Forms validations - not included
- Validate all response code - not included
- Handle all exceptions - not included