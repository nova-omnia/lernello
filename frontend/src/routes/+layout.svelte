<script lang="ts">
    import '../app.css';
    import {writable} from "svelte/store";

    //icons
    import {ChartLine, Files, MessageSquare, Settings, User, House} from 'lucide-svelte';


    let isExpanded = writable(false);
    let {children} = $props();

    function toggleSidebar() {
        isExpanded.update(value => !value);
    }
</script>


<div class="flex items-center justify-center w-screen h-screen space-x-6">
    <!-- Sidebar -->
    <div class="flex flex-col items-center h-full overflow-hidden text-gray-700 bg-gray-100 rounded transition-all duration-300"
         class:isFullWidth={$isExpanded}
         style="width: {$isExpanded ? '200px' : '4rem'}">

        <!-- Expand/Collapse Button -->
        <button
                class="flex items-center justify-center w-12 h-12 mt-3 text-gray-800 rounded"
                on:click={toggleSidebar} aria-label="Toggle sidebar">
            <svg
                    class="w-6 h-6 transition-transform transform"
                    xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"
                    stroke-width="2"
                    style="transform: rotate({$isExpanded ? '0deg' : '180deg'})">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
            </svg>
        </button>
        <!-- Logo -->
        <a class="flex items-center justify-center mt-3" href="#" aria-label="Lernello">
            <svg class="w-8 h-8 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                 fill="currentColor">
                <path d="M11 17a1 1 0 001.447.894l4-2A1 1 0 0017 15V9.236a1 1 0 00-1.447-.894l-4 2a1 1 0 00-.553.894V17zM15.211 6.276a1 1 0 000-1.788l-4.764-2.382a1 1 0 00-.894 0L4.789 4.488a1 1 0 000 1.788l4.764 2.382a1 1 0 00.894 0l4.764-2.382zM4.447 8.342A1 1 0 003 9.236V15a1 1 0 00.553.894l4 2A1 1 0 009 17v-5.764a1 1 0 00-.553-.894l-4-2z"/>
            </svg>
            {#if $isExpanded}<span class="ml-3">Lernello</span>{/if}
        </a>
        <!-- Buttons Section -->
        <div class="flex flex-col items-center mt-3 border-t border-gray-300">
            <a class="flex items-center justify-center w-full h-12 mt-2 rounded hover:bg-gray-300" href="#"
               aria-label="Home">
                <House size={24} />
                {#if $isExpanded}<span class="ml-3">Home</span>{/if}
            </a>
            <a class="flex items-center justify-center w-full h-12 mt-2 rounded hover:bg-gray-300" href="#"
               aria-label="Learning Kit">
                <Files size={24} />
                {#if $isExpanded}<span class="ml-3">Learning Kits</span>{/if}
            </a>
            <a class="flex items-center justify-center w-full h-12 mt-2 rounded hover:bg-gray-300" href="#"
               aria-label="Statistics">
                <ChartLine size={24} />
                {#if $isExpanded}<span class="ml-3">Statistics</span>{/if}
            </a>
        </div>
        <div class="flex flex-col items-center mt-2 border-t border-gray-300">
            <a class="flex items-center justify-center w-full h-12 mt-2 rounded hover:bg-gray-300" href="#"
               aria-label="Settings">
                <Settings size={24} />
                {#if $isExpanded}<span class="ml-3">Settings</span>{/if}
            </a>
            <a class="relative flex items-center justify-center w-full h-12 mt-2 rounded hover:bg-gray-300" href="#"
               aria-label="Notifications">
                <MessageSquare size={24} />
                <span class="absolute top-0.5 left-2.5 w-2 h-2 mt-2 ml-2 bg-indigo-500 rounded-full"></span>
                {#if $isExpanded}<span class="ml-3">Notifications</span>{/if}
            </a>
        </div>
        <a class="flex items-center justify-center w-full h-16 mt-auto bg-gray-200 hover:bg-gray-300" href="#"
           aria-label="Profile">
            <User size={24} />
            {#if $isExpanded}<span class="ml-3">Profile</span>{/if}
        </a>
    </div>
    {@render children()}

</div>
