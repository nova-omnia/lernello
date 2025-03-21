<script lang="ts">
  import '../styles/lernello-theme.css';
  import SuperDebug, { superForm } from 'sveltekit-superforms';

  let { data } = $props();
  const { form, errors, enhance } = superForm(data.form);

</script>

<form
        method="POST"
        use:enhance
        action="?/CreateLearningKit"
>
    <h1>Create Learning Kit</h1>
    <div>
      <label for="name">Name*:</label>
      <input
              type="text"
              name="name" bind:value={$form.name}
              aria-invalid={$errors.name ? 'true' : 'false'} required />
                bind:value={$form.name}
      {#if $errors.name}
        <p class="error">{$errors.name}</p>
      {/if}
    </div>
    <div>
      <label for="description">Description:</label>
      <textarea id="description"
                name="description"
                bind:value={$form.description}>
      </textarea>
    </div>
    <div>
      <label for="defaultLanguage">Default Language*:</label>
      <select id="defaultLanguage"
              name="defaultLanguage"
              bind:value={$form.defaultLanguage}
              aria-invalid={$errors.defaultLanguage ? 'true' : 'false'} required>
        <option value="" disabled>Select a language</option>
        <option value="en">English</option>
        <option value="de">German</option>
        <option value="fr">French</option>
      </select>
      {#if $errors.defaultLanguage}
        <p class="error">{$errors.defaultLanguage}</p>
      {/if}
    </div>
    <div>
      <label for="deadline">Deadline:</label>
      <input
              type="date"
              name="deadline"
              bind:value={$form.deadline} />
    </div>
    <div>
      <label for="startDate">Start Date:</label>
      <input
              type="date"
              name="startDate"
              bind:value={$form.startDate} />
    </div>
    <div>
      <label for="endDate">End Date:</label>
      <input type="date"
             name="endDate"
             bind:value={$form.endDate} />
    </div>
    <div>
      <label for="participants">Participants:</label>
      <textarea
              name="participants"
              bind:value={$form.participants}></textarea>
    </div>
    <button type="submit">Create LearningKit</button>
    <p>*required fields</p>
  <SuperDebug data={$form} />
</form>

<style>
  form {
    display: grid;
    gap: 1rem;
    margin: 1rem;
  }

  label {
    font-weight: bold;
  }

  input, textarea, select {
    width: 100%;
    padding: 0.5rem;
    font-size: 1rem;
    box-sizing: border-box;
  }

  button {
    padding: 0.5rem 1rem;
    font-size: 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 0.25rem;
    cursor: pointer;
  }

  button:hover {
    background-color: #0056b3;
  }

  .error {
    color: red;
    font-size: 0.875rem;
  }
</style>