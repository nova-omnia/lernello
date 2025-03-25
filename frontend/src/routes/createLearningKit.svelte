<script lang="ts">
    import SuperDebug, { superForm } from 'sveltekit-superforms';

    let { data } = $props();
    const { form, errors, enhance } = superForm(data.form);

    function removeFile() {
       //TODO implement remove file
    }

</script>

<form
        method="POST"
        use:enhance
        action="?/CreateLearningKit"
        class="mx-auto w-full space-y-4 p-4 max-w-lg"
>
    <h1 class="text-2xl font-bold">Create a new Learning Kit</h1>

    <div>
        <label for="name" class="block">Name *</label>
        <input
                type="text"
                name="name"
                bind:value={$form.name}
                aria-invalid={$errors.name ? 'true' : 'false'}
                required
                class="w-full p-2 text-lg border border-gray-300 rounded-container"
        />
        {#if $errors.name}
            <p class="text-red-500 text-sm">{$errors.name}</p>
        {/if}
    </div>

    <div>
        <label for="description" class="block">Description *</label>
        <textarea
                id="description"
                name="description"
                bind:value={$form.description}
                class="w-full p-2 text-lg border border-gray-300 rounded-container"
        ></textarea>
    </div>

    <div>
        <label for="deadline" class="block">Deadline</label>
        <input
                type="date"
                name="deadline"
                bind:value={$form.deadline}
                class="w-full p-2 text-lg border border-gray-300 rounded-container"
        />
    </div>

    <div>
        <label for="defaultLanguage" class="block">Default Language *</label>
        <select
                id="defaultLanguage"
                name="defaultLanguage"
                bind:value={$form.defaultLanguage}
                aria-invalid={$errors.defaultLanguage ? 'true' : 'false'}
                required
                class="w-full p-2 text-lg border border-gray-300 rounded-container"
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
        <label for="context" class="block font-medium">Context ✨</label>
        <textarea
                id="context"
                name="additionalContext"
                bind:value={$form.additionalContext}
                class="w-full p-3 text-base border border-gray-300 rounded-container"
                placeholder="Provide additional context..."
        ></textarea>
    </div>

    {#each $form.files as file (file.name)}
        <div class="flex items-center justify-between p-3 border border-gray-300 rounded-container mt-2">
            <div class="flex items-center space-x-2">
                <svg class="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                <span class="text-base text-gray-800">{file.name}</span>
            </div>
            <button
                    type="button"
                    onclick={() => removeFile()}
                    class="px-3 py-1 text-white bg-red-500 rounded-container hover:bg-red-600"
            >
                Remove
            </button>
        </div>
    {/each}

    <label class="label block">
        <input class="input block rounded-container" type="file" />
    </label>


    <button type="submit" class="w-full px-4 py-2 text-lg text-white bg-blue-500 rounded-container hover:bg-blue-700">
        Create
    </button>

    <p class="text-sm">*required fields</p>
    <SuperDebug data={$form} />
</form>
