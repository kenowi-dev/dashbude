<script lang="ts">
    import type {Item} from "$lib/types";

    let {item}: { item: Item } = $props();
</script>

<a href={item.url}
   class="min-w-48 max-w-96 shadow-lg transition-transform transform hover:scale-105 hover:cursor-pointer rounded-xl">
    <div class="text-primary-950-50 flex items-center gap-4 p-5 bg-gray-600  rounded-xl bg-opacity-30 backdrop-blur-xl bg-clip-padding">
        <img src={item.iconUrl} alt={item.name}
             class="w-14 h-14 rounded-lg border-2 border-surface-300-700"/>
        <div>
            <h2 class="text-xl font-semibold">{item.name}</h2>
            {#if item.group}
                <p class="text-sm opacity-80">{item.group}</p>
            {/if}
        </div>
    </div>
</a>