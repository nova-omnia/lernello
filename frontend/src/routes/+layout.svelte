<script lang="ts">
    import '../app.css';
    import {writable} from "svelte/store";

    //icons
    import {ChartLine, Files, MessageSquare, Settings, User, House, GraduationCap, ChevronRight} from 'lucide-svelte';


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
                onclick={toggleSidebar} aria-label="Toggle sidebar" class:rotate-180={$isExpanded}>
            <ChevronRight size={24}/>
        </button>
        <div>
            <!-- Logo -->
            <p class="flex items-center mt-3" aria-label="Lernello">
                <GraduationCap size={24}/>
                {#if $isExpanded}<span class="ml-3">Lernello</span>{/if}
            </p>
            <!-- Buttons Section -->
            <div class="flex flex-col items-center mt-3 border-t border-gray-300">
                <a class="flex items-center w-full h-12 mt-2 rounded" href="/home"
                   aria-label="Home">
                    <House size={24}/>
                    {#if $isExpanded}<span class="ml-3">Home</span>{/if}
                </a>
                <a class="flex items-center w-full h-12 mt-2 rounded" href="/learning-kits"
                   aria-label="Learning Kit">
                    <Files size={24}/>
                    {#if $isExpanded}<span class="ml-3">Learning Kits</span>{/if}
                </a>
                <a class="flex items-center w-full h-12 mt-2 rounded" href="/statistics"
                   aria-label="Statistics">
                    <ChartLine size={24}/>
                    {#if $isExpanded}<span class="ml-3">Statistics</span>{/if}
                </a>
            </div>
            <div class="flex flex-col items-center mt-2 border-t border-gray-300">
                <a class="flex items-center w-full h-12 mt-2 rounded" href="/settings"
                   aria-label="Settings">
                    <Settings size={24}/>
                    {#if $isExpanded}<span class="ml-3">Settings</span>{/if}
                </a>
                <a class="relative flex items-center justify-center w-full h-12 mt-2 rounded" href="/notifications"
                   aria-label="Notifications">
                    <MessageSquare size={24}/>
                    <span class="absolute top-0.5 left-2.5 w-2 h-2 mt-2 ml-2 bg-indigo-500 rounded-full"></span>
                    {#if $isExpanded}<span class="ml-3">Notifications</span>{/if}
                </a>
            </div>
        </div>
        <a class="flex items-center justify-center w-full h-16 mt-auto bg-gray-200" href="/profile"
           aria-label="Profile">
            <User size={24}/>
            {#if $isExpanded}<span class="ml-3">Profile</span>{/if}
        </a>
    </div>
    {@render children()}

</div>
