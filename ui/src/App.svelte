<script lang="ts">
  import CounterTrend from './CounterTrend.svelte';

  const MS_PER_DAY = 24 * 60 * 60 * 1000;

  const now = Date.now();
  const data = [];
  const DAYS = 45;
  for (let i = 0; i < DAYS; i++) {
    data.push({
      timestamp: now - (DAYS - i) * MS_PER_DAY,
      value: Math.round(Math.random() * 30)
    });
  }
  const subtitle = 'Geiger Counter';
  const title = 'Custom Counter Trend';
</script>

<main>
  <CounterTrend {data} {subtitle} {title} />
</main>

<style>
</style>
