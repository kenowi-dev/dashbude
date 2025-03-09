<script lang="ts">
    import LightSwitch from "$lib/components/LightSwitch.svelte";
    import type {PageProps} from './$types';
    import DashboardCard from "$lib/components/DashboardCard.svelte";
    import type {Item} from "$lib/types";


    const {data}: PageProps = $props()

    let groupedItems = $derived(data.dashboard.items.reduce((acc, item) => {
        const group = item.group || "Other"; // Default group if none provided
        if (!acc[group]) acc[group] = [];
        acc[group].push(item);
        return acc;
    }, {} as { [key: string]: Item[] }));
</script>


<div class="min-h-screen h-full flex flex-col ">
    <!-- Top Navbar -->
    <nav class="flex items-center justify-between px-6 py-4 bg-primary-400-600 text-primary-950-50 shadow-md sticky top-0 z-50">
        <div class="flex items-center gap-3">
            <h1 class="text-xl font-bold text-center">🌟 Dashboard</h1>
        </div>
        <div class="flex gap-6">
            <!--
            <a href="/" class="hover:text-secondary-400-600">Home</a>
            <a href="/settings" class="hover:text-secondary-500">Settings</a>
            <a href="/profile" class="hover:text-secondary-500">Profile</a>
            -->
            <LightSwitch/>
        </div>
    </nav>

    <!-- Dashboard -->
    <main class="p-6 flex-1 bg-[url(https://picsum.photos/1920/1080)]  bg-hero bg-no-repeat bg-cover bg-center bg-fixed">
    <!-- <main class="p-6 flex-1 bg-linear-to-t from-primary-100 via-secondary-100 to-tertiary-100  dark:from-primary-900 dark:via-secondary-900 dark:to-tertiary-900 "> -->

        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            {#each Object.entries(groupedItems) as [group, items]}
                <section class="p-4 rounded-lg">
                    <h2 class="text-2xl font-semibold text-primary-950-50">{group}</h2>

                    <div class="grid grid-cols-1 xl:grid-cols-2 2xl:grid-cols-3 gap-4">
                        {#each items as item}
                            <DashboardCard {item} />
                        {/each}
                    </div>
                </section>
            {/each}
        </div>

    </main>
</div>