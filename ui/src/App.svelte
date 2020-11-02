<script lang="ts">
  import {onMount} from 'svelte';
  import CounterTrend from './CounterTrend.svelte';

  const URL_PREFIX = 'http://localhost:8080/api/';

  $: decimalPlaces = 3;
  $: subtitle = '';
  $: title = '';
  $: widgets = [];

  onMount(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    if (token) sessionStorage.setItem('token', token);
  });

  async function myFetch(urlSuffix) {
    const headers = new Headers();
    headers.append('Authorization', 'Bearer ' + token);
    let res = await fetch(URL_PREFIX + urlSuffix, {headers});
    if (res.status === 401) {
      login();
    } else {
      return res.json();
    }
  }

  async function loadData() {
    try {
      let json = await myFetch('widgets');
      if (!json) return; // login
      for (const d of json.data) {
        title = d.title;
        for (const slot of d.slots) {
          subtitle = slot.subtitle;
          const {label} = slot;
          json = await myFetch('values/' + label);
          if (!json) return; // login
          const data = [];
          for (const obj of json.data) {
            const date = new Date(obj.timestamp * 1000);
            obj.day = date.getDate();
            data.push(obj);
          }
          data.reverse(); // to order from oldest to newest
          widgets.push({data, subtitle, title});
        }
      }
      widgets = widgets; // trigger reactivity
    } catch (e) {
      console.error('App.svelte loadData: e =', e);
      alert('loadData error: ' + e.message);
    }
  }

  function login() {
    location.replace('http://localhost:8080/oauth/login/cognito');
  }

  function logout() {
    sessionStorage.removeItem('token');
    location.replace('http://localhost:8080/oauth/logout?token=' + token);
  }

  const token = sessionStorage.getItem('token');
  if (token) {
    loadData();
  } else {
    login();
  }
</script>

<main>
  {#if token}
    <!-- <div>
    <label for="dp">Decimal Places:</label>
    <span>{decimalPlaces}</span>
    <input id="dp" type="range" min="0" max="6" bind:value={decimalPlaces} />
  </div> -->
    <header><button on:click={logout}>Logout</button></header>
    {#each widgets as {data, subtitle, title}}
      <CounterTrend {data} {decimalPlaces} {subtitle} {title} />
    {/each}
  {/if}
</main>

<style>
  button {
    background-color: transparent;
    border: none;
    text-decoration: underline;
  }
  header {
    text-align: right;
  }
</style>
