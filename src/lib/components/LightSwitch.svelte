<script lang="ts">
    import {Sun, Moon} from "lucide-svelte";
    import {toggleMode, mode} from "mode-watcher";
</script>

<button onclick={toggleMode}
        class="hover:cursor-pointer transition-all"
        title="Toggle dark mode.">
    {#if $mode === "dark"}
        <Sun class="text-primary-100 hover:brightness-50"/>
    {:else}
        <Moon class="text-primary-900 hover:brightness-150"/>
    {/if}
</button>

