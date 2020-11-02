<script lang="ts">
  import CounterTrend from './CounterTrend.svelte';

  const URL_PREFIX = 'http://localhost:8080/api/';

  $: decimalPlaces = 3;
  $: subtitle = '';
  $: title = '';
  $: widgets = [];

  async function loadData() {
    try {
      const res = await fetch(URL_PREFIX + 'widgets');
      const json = await res.json();
      for (const d of json.data) {
        title = d.title;
        for (const slot of d.slots) {
          subtitle = slot.subtitle;
          const {label} = slot;
          const res2 = await fetch(URL_PREFIX + 'values/' + label);
          const json2 = await res2.json();
          const data = [];
          for (const obj of json2.data) {
            obj.hour = new Date(obj.timestamp).getHours();
            data.push(obj);
          }
          data.reverse(); // to order from oldest to newest
          widgets.push({data, subtitle, title});
        }
      }
      widgets = widgets; // trigger reactivity
      console.log('App.svelte x: widgets =', widgets);
    } catch (e) {
      console.error('App.svelte loadData: e =', e);
      alert('loadData error: ' + e.message);
    }
  }

  loadData();
</script>

<main>
  <div>
    <label for="dp">Decimal Places:</label>
    <span>{decimalPlaces}</span>
    <input id="dp" type="range" min="0" max="6" bind:value={decimalPlaces} />
  </div>
  {#each widgets as {data, subtitle, title}}
    <CounterTrend {data} {decimalPlaces} {subtitle} {title} />
  {/each}
</main>

<style>
  div {
    display: flex;
    align-items: center;
  }

  label {
    margin-right: 0.5rem;
  }

  span {
    margin-right: 0.5rem;
  }
</style>
