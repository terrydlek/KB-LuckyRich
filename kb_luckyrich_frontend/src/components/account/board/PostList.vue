<template>
  <div id="container">
    <div id="content">
      <div class="container mt-5">
        <h2 class="mb-4">Q & A</h2>
        <table class="table table-striped">
          <thead class="thead-dark">
            <tr>
              <th>No</th>
              <th>제목</th>
              <th>글쓴이</th>
              <th>작성일</th>
            </tr>
          </thead>
          <tbody v-if="posts">
            <tr v-for="post in posts" :key="post.boardNum">
              <td>{{ post.boardNum }}</td>
              <td>
                <a :href="`/luckyrich/qa/ask/${post.boardNum}`">{{
                  post.title
                }}</a>
              </td>
              <td>{{ post.nickName }}</td>
              <td>{{ new Date(post.createdAt).toLocaleString() }}</td>
            </tr>
          </tbody>
        </table>
        <a href="/luckyrich/qa/ask/create" id="bt" class="btn btn-primary"
          >글쓰기</a
        >
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      posts: [],
    };
  },
  mounted() {
    axios
      .get('http://localhost:8080/board/getList', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('access_token')}`,
        },
      })
      .then((response) => {
        this.posts = response.data;
      })
      .catch((error) => {
        console.error('There was an error fetching the posts:', error);
      });
  },
};
</script>

<style>
@charset "utf-8";

/* 웹폰트 */
@import url('https://fonts.googleapis.com/css?family=Righteous');

@font-face {
  font-family: 'HSBombaram';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/HSBombaram.woff')
    format('woff');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'Iropke Batang';
  font-style: normal;
  font-weight: 400;
  src: url(//cdn.jsdelivr.net/font-iropke-batang/1.2/IropkeBatangM.eot);
  src: url(//cdn.jsdelivr.net/font-iropke-batang/1.2/IropkeBatangM.eot?#iefix)
      format('embedded-opentype'),
    url(//cdn.jsdelivr.net/font-iropke-batang/1.2/IropkeBatangM.woff)
      format('woff'),
    url(//cdn.jsdelivr.net/font-iropke-batang/1.2/IropkeBatangM.ttf)
      format('truetype');
}

/* 전체 폰트 */
#container {
  font-family: 'Iropke Batang', sans-serif;
  line-height: 2;
  font-size: 13px;
  color: #444;
  color: var(--text);
}

.container {
  padding: 40px 0;
}

h2 {
  font-family: 'HSBombaram';
  font-size: 2.5em;
  color: #f8b400;
  margin-bottom: 20px;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1em;
}

.table th,
.table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.table-striped tbody tr:nth-of-type(odd) {
  background-color: rgba(0, 0, 0, 0.05);
}

.table a {
  color: #b58b71;
  color: var(--point);
  text-decoration: none;
  transition: 0.5s;
}

.table a:hover {
  opacity: 0.5;
}

#bt {
  margin-top: 20px;
  margin-bottom: 20px;
  background-color: #f8b400;
  border: none;
  color: white;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  transition: background-color 0.3s;
}

#bt:hover {
  background-color: #e69c00;
}

/* 반응형 디자인 */
@media screen and (max-width: 768px) {
  .container {
    padding: 20px 15px;
  }

  .table,
  .table thead,
  .table tbody,
  .table th,
  .table td,
  .table tr {
    display: block;
  }

  .table thead tr {
    position: absolute;
    top: -9999px;
    left: -9999px;
  }

  .table tr {
    border: 1px solid #ccc;
    margin-bottom: 10px;
  }

  .table td {
    border: none;
    border-bottom: 1px solid #eee;
    position: relative;
    padding-left: 50%;
  }

  .table td:before {
    position: absolute;
    top: 6px;
    left: 6px;
    width: 45%;
    padding-right: 10px;
    white-space: nowrap;
  }

  .table td:nth-of-type(1):before {
    content: 'No';
  }
  .table td:nth-of-type(2):before {
    content: '제목';
  }
  .table td:nth-of-type(3):before {
    content: '글쓴이';
  }
  .table td:nth-of-type(4):before {
    content: '작성일';
  }
}

@media screen and (max-width: 425px) {
  h2 {
    font-size: 2rem;
  }

  #bt {
    width: 100%;
  }
}
</style>
