<script lang="ts">
    import SuperDebug, { superForm } from 'sveltekit-superforms';

    let { data } = $props();
    const { form, errors, enhance } = superForm(data.form);
</script>

<form
        method="POST"
        use:enhance
        action="?/CreateLearningKit"
        class="grid gap-4 p-4"
>
    <h1 class="text-2xl font-bold">Create Learning Kit</h1>
    <div>
        <label for="name" class="font-bold">Name*:</label>
        <input
                type="text"
                name="name"
                bind:value={$form.name}
                aria-invalid={$errors.name ? 'true' : 'false'}
                required
                class="w-full p-2 text-lg border border-gray-300 rounded"
        />
        {#if $errors.name}
            <p class="text-red-500 text-sm">{$errors.name}</p>
        {/if}
    </div>
    <div>
        <label for="description" class="font-bold">Description:</label>
        <textarea
                id="description"
                name="description"
                bind:value={$form.description}
                class="w-full p-2 text-lg border border-gray-300 rounded"
        ></textarea>
    </div>
    <div>
        <label for="defaultLanguage" class="font-bold">Default Language*:</label>
        <select
                id="defaultLanguage"
                name="defaultLanguage"
                bind:value={$form.defaultLanguage}
                aria-invalid={$errors.defaultLanguage ? 'true' : 'false'}
                required
                class="w-full p-2 text-lg border border-gray-300 rounded"
        >
            <option value="" disabled>Select a language</option>
            <option value="en">English</option>
            <option value="de">German</option>
            <option value="fr">French</option>
        </select>
        {#if $errors.defaultLanguage}
            <p class="text-red-500 text-sm">{$errors.defaultLanguage}</p>
        {/if}
    </div>
    <div>
        <label for="deadline" class="font-bold">Deadline:</label>
        <input
                type="date"
                name="deadline"
                bind:value={$form.deadline}
                class="w-full p-2 text-lg border border-gray-300 rounded"
        />
    </div>
    <div>
        <label for="startDate" class="font-bold">Start Date:</label>
        <input
                type="date"
                name="startDate"
                bind:value={$form.startDate}
                class="w-full p-2 text-lg border border-gray-300 rounded"
        />
    </div>
    <div>
        <label for="endDate" class="font-bold">End Date:</label>
        <input
                type="date"
                name="endDate"
                bind:value={$form.endDate}
                class="w-full p-2 text-lg border border-gray-300 rounded"
        />
    </div>
    <div>
        <label for="participants" class="font-bold">Participants:</label>
        <textarea
                name="participants"
                bind:value={$form.participants}
                class="w-full p-2 text-lg border border-gray-300 rounded"
        ></textarea>
    </div>
    <button type="submit" class="px-4 py-2 text-lg text-white bg-blue-500 rounded hover:bg-blue-700">
        Create LearningKit
    </button>
    <p class="text-sm">*required fields</p>
    <SuperDebug data={$form} />
</form>