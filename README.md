# 지원자 성명

안녕하세요. 지원자 **`김종성`** 입니다.

<br>
<br>

# ERD

![erd](https://github.com/eello/wanted-pre-onboarding-backend/assets/33685064/f068c574-0b3e-4f44-9024-fd189ad41e41)

<br>
<br>

# 프로젝트 구조
![project architecture](https://github.com/eello/wanted-pre-onboarding-backend/assets/33685064/d2177bbd-50cc-4671-b061-d9b275d6e44b)

<br>
<br>

# 애플리케이션 실행 방법
### 1. 데이터베이스 `ddl.sql` 실행
<details>
<summary><strong>ddl.sql</strong></summary>

```sql
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema wpob
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema wpob
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `wpob` DEFAULT CHARACTER SET utf8 ;
USE `wpob` ;

-- -----------------------------------------------------
-- Table `wpob`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wpob`.`users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(255) NULL,
  `refresh_token` VARCHAR(255) NULL,
  `created_at` TIMESTAMP NULL,
  `updated_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wpob`.`boards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wpob`.`boards` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL,
  `content` TEXT NULL,
  `writer_id` BIGINT NOT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_boards_users_idx` (`writer_id` ASC) VISIBLE,
  CONSTRAINT `fk_boards_users`
    FOREIGN KEY (`writer_id`)
    REFERENCES `wpob`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
```
</details>

### 2. 애플리케이션 실행

<details>
<summary><strong>Git Clone으로 실행</strong></summary>

1. Git Repository Clone
    ```
    git clone https://github.com/eello/wanted-pre-onboarding-backend.git
    ```

2. `application-dev.yml` 수정
    ```yaml
    spring:
        datasource:
            url: ${your mysql db.url}
            driver-class-name: com.mysql.cj.jdbc.Driver
            username: ${your db.username}
            password: ${your db.password}
        jpa:
            hibernate:
                ddl-auto: none
            properties:
                hibernate:
                    show_sql: false
                    format_sql: false
            database: mysql
            database-platform: org.hibernate.dialect.MySQL5InnoDBDialect

        jwt:
            secret: ${your jwt.secret}
    ```
3. 어플리케이션 실행
</details>

<details>
<summary><strong>Docker Image(docker-compose)로 실행</strong></summary>

1. `docker-compose.yml` 생성
    ```yaml
    version: '3'

    services:
            backend:
                image: eello/wanted-pre-onboarding-backend
                    environment:
                            - PROFILE=deploy
                            - DB_USERNAME=your_db_username
                            - DB_PASSWORD=your_db_password
                            - DB_URL=your_mysql_db_url
                            - JWT_SECRET=your_jwt_secret
                    ports:
                            - 8080:8080
                    restart: always
    ```
2. `docker-compose` 실행
    ```bash
    docker-compose up
    ```
</details>

### 3. API 호출
#### BaseUrl: `https://eello.site`
##### 1. 사용자 회원가입
- 엔드포인트: `POST /api/users`
- 본문
    | Name     | Type   | Required | Description  |
    |----------|--------|----------|--------------|
    | email    | String | O        | '@'를 포함한 문자열 |
    | password | String | O        | 8자 이상        |

##### 2. 사용자 로그인
- 엔드포인트: `POST /api/users/login`
- 본문
    | Name     | Type   | Required | Description  |
    |----------|--------|----------|--------------|
    | email    | String | O        | '@'를 포함한 문자열 |
    | password | String | O        | 8자 이상        |

##### 3. 새로운 게시글 생성
- 엔드포인트: `POST /api/boards`
- 인증방식: 엑세스 토큰
- 헤더
    | Name           | Value                  |
    |---------------|------------------------|
    | Authorization | Bearer ${ACCESS_TOKEN} |
- 본문
    | Name    | Type   | Required | Description        |
    |---------|--------|----------|--------------------|
    | title   | String | O        | 공백이 아닌 글자 1자 이상 포함 |
    | content | String | O        | 공백이 아닌 글자 1자 이상 포함 |


##### 4. 게시글 목록 조회
- 엔드포인트: `GET /api/boards`
- Query Parameter
    | Name | Type    | Required | Description      | Default |
    |------|---------|----------|------------------|---------|
    | page | Integer | X        | 조회하려는 페이지 번호     | 0       |
    | size | Integer | X        | 한 페이지에 포함된 데이터 수 | 20      |

##### 5. 특정 게시글 조회
- 엔드포인트: `GET /api/boards/:boardId`
- Path Variable
    | Name    | Type    | Description   |
    |---------|---------|---------------|
    | boardId | Integer | 조회하려는 게시글의 ID |

##### 6. 특정 게시글 수정
- 엔드포인트: `PATCH /api/boards/:boardId`
- 인증방식: 엑세스 토큰
- 헤더
    | Name           | Value                  |
    |---------------|------------------------|
    | Authorization | Bearer ${ACCESS_TOKEN} |
- Path Variable
    | Name    | Type    | Description   |
    |---------|---------|---------------|
    | boardId | Integer | 수정하려는 게시글의 ID |
- 본문
    | Name    | Type   | Required | Description        |
    |---------|--------|----------|--------------------|
    | title   | String | X        | 공백이 아닌 글자 1자 이상 포함 |
    | content | String | X        | 공백이 아닌 글자 1자 이상 포함 |

##### 7. 특정 게시글 삭제
- 엔드포인트: `DELETE /api/boards/:boardId`
- 인증방식: 엑세스 토큰
- 헤더
    | Name           | Value                  |
    |---------------|------------------------|
    | Authorization | Bearer ${ACCESS_TOKEN} |
- Path Variable
    | Name    | Type    | Description   |
    |---------|---------|---------------|
    | boardId | Integer | 삭제하려는 게시글의 ID |

<br>
<br>

# 데모 영상
[demo](https://drive.google.com/file/d/12B1LjPSB0alMxguF0-Mk_rnrTEeRdf0G/view)

<br>
<br>

# API 명세
<details>
<summary><strong>1. 사용자 회원가입 엔드포인트</strong></summary>

- ***Request***
    - Method: `POST`
    - Url: `https://eello.site/api/users`
    - Body
        ```json
        {
            "email": "이메일", // '@'를 포함한 문자열
            "password": "비밀번호" // 8자 이상
        }
        ```

- ***Resposne***
  - Success
    - **`201 Created`**
  - Fail
    - **`400 BadRequest`**: 이메일 또는 비밀번호 유효성 검사 실패
    - **`409 Conflict`**: 이메일 중복 (이미 가입된 이메일)

</details>

<details>
<summary><strong>2. 사용자 로그인 엔드포인트</strong></summary>

- ***Request***
    - Method: `POST`
    - Url: `https://eello.site/api/users/login`
    - Body:
        ```json
        {
            "email": "이메일", // '@'를 포함한 문자열
            "password": "비밀번호" // 8자 이상
        }
        ```
- ***Response***
    - Success
        - **`200 OK`**
        - Set-Cookie
            ``` json
                "WPOBRefreshToken": "발급된 refresh token" // Http-Only
            ```
        - Body
            ```json
            {
                "accessToken": "발급된 access token"
            }
            ```
    - Fail
        - **`400 BadRequest`**
            - 이메일에 해당하는 유저가 존재하지 않음
            - 이메일과 비밀번호가 매칭되지 않음
            - 이메일과 비밀번호 유효성 검사 실패
</details>

<details>
<summary><strong>3. 새로운 게시글 생성 엔드포인트</strong></summary>

- ***Request***
    - Method: `POST`
    - Url: `https://eello.site/api/boards`
    - Header
        ``` json
            "Authorization": "Bearer ${ACCESS_TOKEN}"
        ```
    - Body
        ``` json
        {
            "title": "게시글 제목", // 공백만 X, 1자 이상
            "content": "게시글 본문" // 공백만 X, 1자 이상
        }
        ```

- ***Response***
    - Success
        - **`201 Created`**
        - Header
            ``` json
                "Location": "생성된 자원에 대한 uri"
            ```
    - Fail
        - **`400 BadRequest`**: 제목과 본문 유효성 검사 실패
        - **`401 Unauthorized`**: 인증되지 않은 요청
</details>


<details>
<summary><strong>4. 게시글 목록 조회 엔드포인트</strong></summary>

- ***Request***
    - Method: `GET`
    - Url: `https://eello.site/api/boards`
    - Query Parameter
        - page
            - 조회할 페이지 번호
            - default: 0
            - 0부터 시작
        - size
            - 페이지당 데이터 수
            - default: 20
- ***Response***
    - Success
        - **`200 OK`**
        - Body
            ``` json
            {
                "isFirst": "첫 페이지 유무",
                "isLast": "마지막 페이지 유무",
                "isEmpty": "조회된 데이터 유무",
                "hasPrevious": "이전 페이지(pageNum-1)의 존재 유무",
                "hasNext": "다음 페이지(pageNum+1)의 존재 유무",
                "sizePerPage": "페이지당 데이터 수",
                "totalPages": "전체 페이지 수",
                "pageNum": "페이지 번호",
                "totalElements": "전체 데이터 수",
                "content": [
                    {
                        "writer": {
                            "id": "작성자 id",
                            "email": "작성자 이메일"
                        },
                        "id": "게시글 id",
                        "title": "게시글 제목",
                    },
                    {
                        "writer": {
                            "id": "작성자 id",
                            "email": "작성자 이메일"
                        },
                        "id": "게시글 id",
                        "title": "게시글 제목",
                    },
                    ...
                ] // 해당 페이지에 조회된 데이터가 없을 경우 빈 리스트(`[]`) 반환
            }
            ```
</details>


<details>
<summary><strong>5. 특정 게시글 조회 엔드포인트</strong></summary>

- ***Request***
    - Method: `GET`
    - Url: `https://eello.site/api/boards/:boardId`
    - Path Variable
        - :boardId
            - 조회하려는 게시글의 id
- ***Response***
    - Success
        - **`200 OK`**
        - Body
            ``` json
            {
                "writer": {
                    "id": "작성자 id",
                    "email": "작성자 이메일"
                },
                "id": "게시글 id",
                "title": "게시글 제목",
                "content": "본문"
            }	
            ```
    - Fail
        - **`404 NotFound`**: 조회하려는 게시글의 id(boardId)에 해당하는 게시글이 존재하지 않음
</details>


<details>
<summary><strong>6. 특정 게시글 수정 엔드포인트</strong></summary>

- ***Request***
    - Method: `PATCH`
    - Url: `https://eello.site/api/boards/:boardId`
    - Path Variable
        - :boardId
            - 수정하려는 게시글의 id
    - Header
        ``` json
            "Authorization": "Bearer ${ACCESS_TOKEN}"
        ```
    - Body
        ``` json
        {
            "title": "게시글 제목", // 공백만 X, 1자 이상, Required X
            "content": "게시글 본문" // 공백만 X, 1자 이상, Required X
        }
        ```

- ***Response***
    - Success
        - **`204 NoContent`**
    - Fail
        - **`401 Unauthorized`**: 인증되지 않은 요청
        - **`403 Forbidden`**: 작성자가 아닌 유저의 요청
        - **`404 NotFound`**: boardId에 해당하는 게시글이 존재하지 않음
</details>


<details>
<summary><strong>7. 특정 게시글 삭제 엔드포인트</strong></summary>

- ***Request***
    - Method: `DELETE`
    - Url: `https://eello.site/api/boards/:boardId`
    - Path Variable
        - :boardId
            - 삭제하려는 게시글의 id
    - Header
        ``` json
            "Authorization": "Bearer ${ACCESS_TOKEN}"
        ```
- ***Response***
    - Success
        - **`204 NoContent`**
    - Fail
        - **`401 Unauthorized`**: 인증되지 않은 요청
        - **`403 Forbidden`**: 작성자가 아닌 유저의 요청
        - **`404 NotFound`**: boardId에 해당하는 게시글이 존재하지 않음
</details>
