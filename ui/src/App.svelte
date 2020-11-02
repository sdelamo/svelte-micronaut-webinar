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

  async function loadData() {
    const headers = new Headers();
    headers.append('Authorization', 'Bearer ' + token);

    try {
      const res = await fetch(URL_PREFIX + 'widgets', {headers});
      const json = await res.json();
      for (const d of json.data) {
        title = d.title;
        for (const slot of d.slots) {
          subtitle = slot.subtitle;
          const {label} = slot;
          const res2 = await fetch(URL_PREFIX + 'values/' + label, {headers});
          const json2 = await res2.json();
          const data = [];
          for (const obj of json2.data) {
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

  function logout() {
    sessionStorage.removeItem('token');
    location.replace('http://localhost:8080/oauth/logout');
  }

  const token = sessionStorage.getItem('token');
  if (token) {
    loadData();
  } else {
    location.replace('http://localhost:8080/oauth/login/cognito');
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
