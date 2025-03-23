<script lang="ts">
    let learningUnitName: string = "";
    let error: string = "";

    interface LearningUnit {
        uuid: string;
    }

    async function handleSubmit(event: Event): Promise<void> {
        event.preventDefault();
        error = "";

        if (!learningUnitName.trim()) {
            error = "Learning Unit name is required.";
            return;
        }

        try {
            const response = await fetch("/api/learning-units", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name: learningUnitName.trim() })
            });
            if (!response.ok) {
                throw new Error("Failed to create");
            }
            const createdUnit: LearningUnit = await response.json();
            window.location.href = `/edit/${createdUnit.uuid}`;
        } catch (e: unknown) {
            if (e instanceof Error) {
                error = e.message;
            } else {
                error = "An unknown error occurred";
            }
        }
    }
</script>

<div class="flex items-center justify-center h-full">
    <form on:submit|preventDefault={handleSubmit} class="max-w-md mx-auto bg-white p-6 rounded-lg shadow-md">
        <h2 class="text-2xl font-bold mb-4">Create Learning Unit</h2>
        <div class="mb-4">
            <label for="learningUnitName" class="block text-sm font-medium mb-1">Learning Unit Name</label>
            <input
                    id="learningUnitName"
                    type="text"
                    bind:value={learningUnitName}
                    placeholder="Enter learning unit name"
                    required
                    class="w-full border border-gray-300 rounded py-2 px-3 focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
        </div>
        {#if error}
            <div class="mb-4">
                <p class="text-red-500">{error}</p>
            </div>
        {/if}
        <div>
            <button
                    type="submit"
                    class="w-full bg-primary-500 hover:bg-primary-600 text-white font-medium py-2 px-4 rounded focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
                Create
            </button>
        </div>
    </form>
</div>