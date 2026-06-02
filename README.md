# 🏆 Jogos Universitários — JoiaApp2026

Aplicativo Android para acompanhamento dos **Jogos Universitários**, permitindo que alunos acompanhem jogos ao vivo, resultados, próximas partidas e o ranking dos cursos em tempo real.

---

## 📱 Telas

| Tela | Descrição |
|------|-----------|
| **Login** | Autenticação com e-mail e senha |
| **Cadastro** | Registro de novo atleta com seleção de curso |
| **Home** | Jogos ao vivo em carrossel + próximas partidas com filtros |
| **Resultados** | Jogos finalizados com placar e filtro por modalidade |
| **Ranking** | Classificação geral dos cursos + tabela detalhada por modalidade |
| **Perfil** | Dados do usuário logado com opção de logout |

---

## 🛠️ Stack

- **Linguagem:** Kotlin
- **Arquitetura:** MVVM (Model-View-ViewModel)
- **Mínimo SDK:** 26 (Android 8.0)
- **Target SDK:** 35 (Android 15)

### Dependências principais

| Biblioteca | Versão | Uso |
|-----------|--------|-----|
| Retrofit | 2.9.0 | Cliente HTTP |
| Moshi | 1.15.1 | Desserialização JSON (Kotlin-safe) |
| OkHttp Logging | 4.12.0 | Log de requisições |
| Coroutines | 1.7.3 | Chamadas assíncronas |
| ViewModel + LiveData | 2.7.0 | Ciclo de vida seguro |
| Navigation Component | 2.7.7 | Navegação entre fragments |
| SwipeRefreshLayout | 1.1.0 | Pull-to-refresh |
| Material Design 3 | 1.12.0 | Componentes visuais |

---

## 🏗️ Estrutura do projeto

```
app/src/main/java/com/matheusramalho/joiaapp2026/
│
├── data/
│   ├── api/
│   │   ├── AuthApi.kt          # POST /auth/login, /auth/register, GET /users/me, PATCH /users/me
│   │   ├── GameApi.kt          # GET /jogos, /modalidades, /equipes
│   │   ├── RankingApi.kt       # GET /ranking/geral, /ranking/modalidade/{id}
│   │   └── RetrofitClient.kt   # Singleton Retrofit + Moshi
│   │
│   ├── model/
│   │   ├── AuthModels.kt       # LoginRequest, LoginResponse, UserResponse, UpdateProfileRequest
│   │   ├── GameModels.kt       # JogoResponse, EquipeResponse, CursoSimples, ModalidadeSimples
│   │   ├── ModalidadeResponse.kt
│   │   └── RankingModels.kt    # RankingGeralItem, RankingTabelaItem, RankingModalidadeResponse
│   │
│   └── repository/
│       ├── AuthRepository.kt   # Login, registro, perfil
│       ├── GameRepository.kt   # Jogos, modalidades, equipes
│       └── RankingRepository.kt
│
├── ui/
│   ├── Auth/
│   │   ├── LoginFragment.kt
│   │   ├── RegisterFragment.kt
│   │   ├── AuthViewModel.kt
│   │   └── AuthViewModelFactory.kt
│   │
│   ├── home/
│   │   ├── HomeFragment.kt
│   │   ├── HomeViewModel.kt
│   │   ├── JogoLiveAdapter.kt   # Carrossel ao vivo com placar
│   │   └── JogoProximoAdapter.kt
│   │
│   ├── resultados/
│   │   ├── ResultadosFragment.kt
│   │   ├── ResultadosViewModel.kt
│   │   └── ResultadosAdapter.kt  # Exibe placar final
│   │
│   ├── ranking/
│   │   ├── RankingFragment.kt
│   │   ├── RankingViewModel.kt
│   │   ├── RankingAdapter.kt        # Ranking geral por curso
│   │   ├── RankingTabelaAdapter.kt  # Tabela J/V/E/D/SG/PTS por modalidade
│   │   └── RankingViewModelFactory.kt
│   │
│   └── PerfilFragment.kt
│
├── utils/
│   ├── SessionManager.kt  # JWT em SharedPreferences
│   └── Resource.kt        # Sealed class Loading/Success/Error
│
├── AuthActivity.kt        # Login e Cadastro
├── HomeActivity.kt        # App principal com Bottom Navigation
└── MainActivity.kt        # Splash — verifica sessão
```

---

## 🔗 Endpoints consumidos

### Auth
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/auth/login` | Login com e-mail e senha |
| `POST` | `/auth/register` | Cadastro de novo atleta |
| `GET` | `/users/me` | Dados do usuário logado |
| `PATCH` | `/users/me` | Atualiza perfil do usuário |

### Jogos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/jogos` | Lista todos os jogos (com equipes aninhadas) |
| `GET` | `/jogos/{id}` | Detalhe de um jogo |
| `GET` | `/modalidades` | Lista modalidades |
| `GET` | `/equipes` | Lista equipes (filtro por cursoId e modalidadeId) |

### Ranking
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/ranking/geral` | Classificação geral dos cursos por pontos |
| `GET` | `/ranking/modalidade/{id}` | Tabela detalhada de uma modalidade |

---

## 🔄 Status dos jogos

| Status da API | Exibido como |
|--------------|-------------|
| `EM_ANDAMENTO` | ● AO VIVO (carrossel da Home) |
| `AGENDADO` | Próximas partidas (Home) |
| `FINALIZADO` | Resultados (com placar) |
| `CANCELADO` | Não exibido |
| `ADIADO` | Não exibido |

---

## 🔁 Polling ao vivo

O carrossel de jogos ao vivo atualiza automaticamente a cada **30 segundos** usando `repeatOnLifecycle(STARTED)` — o polling pausa automaticamente quando o app vai para background e retoma ao voltar, economizando bateria e dados.

```kotlin
// HomeViewModel.kt
companion object {
    private const val POLLING_MS = 30_000L
}

fun startPolling() {
    viewModelScope.launch {
        while (true) {
            fetchJogos()
            delay(POLLING_MS)
        }
    }
}
```

---

## 🎨 Design System

### Modo claro
| Token | Cor |
|-------|-----|
| Primary | `#534AB7` |
| Background | `#F8F7FF` |
| Surface | `#FFFFFF` |
| On Surface Variant | `#49476B` |

### Modo escuro
| Token | Cor |
|-------|-----|
| Primary | `#8B74F9` |
| Background | `#0C0B18` |
| Surface | `#17152C` |
| On Surface Variant | `#A898FB` |

### Responsividade
| Breakpoint | Dispositivo | `auth_padding_horizontal` |
|-----------|-------------|--------------------------|
| Padrão | Mobile | `24dp` |
| `sw600dp` | Tablet | `80dp` |
| `sw900dp` | Desktop | `160dp` |

---

## 🚀 Como rodar

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 11+
- Android SDK 35

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/matheusramalho/joiaapp2026.git

# 2. Abra no Android Studio
# File → Open → selecione a pasta do projeto

# 3. Sync das dependências
# Android Studio → Sync Now

# 4. Rode o app
# Run → Run 'app' (Shift + F10)
```

> A URL base da API já está configurada no `RetrofitClient.kt`:
> ```
> https://utf60vh8hyb7y44yzsmiw0n1.187.127.5.61.sslip.io/
> ```

---

## 🔐 Autenticação

O token JWT é salvo em `SharedPreferences` via `SessionManager`. Ele é injetado automaticamente no header `Authorization: Bearer {token}` em todas as requisições autenticadas.

O fluxo de abertura do app:

```
App abre → MainActivity
    ↓
SessionManager.isLoggedIn()?
    ├── Sim → HomeActivity (bottom navigation)
    └── Não → AuthActivity (Login / Cadastro)
```

---

## 📋 Ranking — Tabela de modalidade

A aba **Por modalidade** exibe a tabela completa de classificação:

| Col | Significado |
|-----|------------|
| **J** | Jogos disputados |
| **V** | Vitórias |
| **E** | Empates |
| **D** | Derrotas |
| **SG** | Saldo de gols |
| **PTS** | Pontos |

---

## 👥 Roles

| Role | Acesso |
|------|--------|
| `ATLETA` | Visualização de jogos, resultados, ranking e perfil |
| `ADMIN` | Mesmo acesso + gerenciamento via app DEV (JoiaDEV) |

---

*Desenvolvido para os Jogos Universitários 2026 🎓*
