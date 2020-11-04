<script lang="ts">
  export let data: {
    day?: number;
    period: number;
    timestamp: number;
    value: number;
  }[];
  export let decimalPlaces;
  export let title: string;
  export let subtitle: string;

  const CARD_PADDING = 16; // 1rem
  const SECTION_WIDTH = 399;
  const CHART_HEIGHT = 130;
  const CHART_WIDTH = SECTION_WIDTH - CARD_PADDING - 6;
  const DAYS_IN_MONTH = 31; // It's okay that not all months have this many.
  const BAR_WIDTH = CHART_WIDTH / DAYS_IN_MONTH;

  $: lastValue = data.length ? data[data.length - 1].value : 0;
  $: prevValue = data.length ? data[data.length - 2].value : 0;
  $: lastDelta = lastValue - prevValue;
  $: minValue = data.reduce(
    (acc, item) => Math.min(acc, item.value),
    Number.MAX_VALUE
  );
  $: maxValue = data.reduce((acc, item) => Math.max(acc, item.value), 0);
  $: valueRange = maxValue - minValue;
  $: viewBox = `0 0 ${CHART_WIDTH} ${CHART_HEIGHT}`;

  $: hoverBarIndex = -1;
  $: hoverText = 0;
  $: hoverTransform = '';
  $: hoverX = 0;
  $: hoverY = 0;

  const getBarClass = (index: number) =>
    index % 2 === 0 ? 'even-bar' : 'odd-bar';

  function handleHover(index: number, item, event) {
    hoverBarIndex = index;
    const {target} = event;
    const x = target.getAttribute('x');
    hoverX = Number(x) + BAR_WIDTH / 2;
    hoverY = CHART_HEIGHT - 35;
    hoverText = item.value.toFixed(decimalPlaces);
    hoverTransform = `rotate(-90, ${hoverX + 4}, ${hoverY})`;
  }
</script>

<section>
  <div class="title">{title}</div>

  <svg height={CHART_HEIGHT} width={CHART_WIDTH} {viewBox}>
    {#each data as item, index}
      <rect
        class={getBarClass(index)}
        class:hover={index === hoverBarIndex}
        x={BAR_WIDTH * index}
        y={((maxValue - item.value) / valueRange) * CHART_HEIGHT}
        width={BAR_WIDTH}
        height={((item.value - minValue) / valueRange) * CHART_HEIGHT}
        on:mouseover={event => handleHover(index, item, event)}
        on:mouseout={() => (hoverBarIndex = -1)}>
        {item.day}
        -
        {item.value}
      </rect>
    {/each}
    {#each data as item, index}
      {#if index % 5 === 0}
        <text class="day" x={BAR_WIDTH * (index + 0.5)} y={CHART_HEIGHT - 2}>
          {item.day}
        </text>
      {/if}
    {/each}
    {#if hoverBarIndex !== -1}
      <text
        class="hover-text"
        text-anchor="middle"
        transform={hoverTransform}
        x={hoverX}
        y={hoverY}>
        {hoverText}
      </text>
    {/if}
  </svg>

  <div class="last-value">{lastValue.toFixed(decimalPlaces)}</div>
  <div class="subtitle">{subtitle}</div>
  <div class="last-delta">
    {lastDelta >= 0 ? '+' : ''}{lastDelta.toFixed(decimalPlaces)}
  </div>
</section>

<style>
  .day {
    fill: white;
    text-anchor: middle;
  }

  .even-bar {
    fill: var(--blue);
  }

  .hover {
    fill: purple !important;
  }

  .hover-text {
    pointer-events: none;
    stroke: white;
    transform: rotate(90);
  }

  .last-delta {
    position: absolute;
    bottom: 3rem;
    right: 1rem;

    color: var(--red);
    font-size: 1.4rem;
    font-weight: bold;
    pointer-events: none;
  }

  .last-value {
    color: var(--yellow);
    position: absolute;
    bottom: 4.3rem;
    left: 2rem;

    font-size: 4rem;
    font-weight: bold;
    height: 4rem;
    pointer-events: none;
  }

  .odd-bar {
    fill: var(--green);
  }

  section {
    --blue: #2e6bdf;
    --green: #53b735;
    --red: #eb4727;
    --purple: #8c1bb9;
    --yellow: #f6c944;

    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;

    background-color: transparent;
    border: solid #eee4d5 10px;
    border-radius: 0.5rem;
    box-sizing: border-box;
    color: darkgray;
    position: relative;
    width: 400px;
  }

  .subtitle {
    color: var(--red);
    position: absolute;
    bottom: 3rem;
    left: 2rem;

    font-size: 1.2rem;
    font-weight: bold;
    pointer-events: none;
  }

  svg text {
    fill: var(--red);
    font-size: 0.7rem;
  }

  .title {
    box-sizing: border-box;
    color: var(--purple);
    font-size: 0.8rem;
    font-weight: bold;
    padding: 0.5rem;
    pointer-events: none;
    text-align: right;
    text-transform: uppercase;
    width: 100%;
  }
</style>
