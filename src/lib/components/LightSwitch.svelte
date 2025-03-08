<script lang="ts">
    import {Sun, Moon} from "lucide-svelte";
    import {toggleMode} from "mode-watcher";
</script>

<button onclick={toggleMode} class="btn hover:preset-tonal" title="Toggle dark mode.">
    <Sun class="h-[1.2rem] w-[1.2rem] scale-100 dark:scale-0"/>
    <Moon class="absolute h-[1.2rem] w-[1.2rem] scale-0 dark:scale-100"/>
</button>

