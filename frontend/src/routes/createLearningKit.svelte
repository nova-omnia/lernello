<script lang="ts">
  import '../styles/lernello-theme.css';

  let name = '';
  let description = '';
  let defaultLanguage = '';
  let deadline = '';
  let startDate = '';
  let endDate = '';
  let participants = '';

  const handleSubmit = (event: Event) => {
    event.preventDefault();
    const learningKit = { name, description, defaultLanguage, deadline, startDate, endDate, participants };
    console.log('LearningKit created:', learningKit);
    // TODO: handle learningKit creation
  };
</script>

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
</style>

<form method="POST" on:submit={handleSubmit}>
  <h1>Create Learning Kit</h1>
  <div>
    <label for="name">Name (required):</label>
    <input type="text" id="name" bind:value={name} required />
  </div>
  <div>
    <label for="description">Description (optional):</label>
    <textarea id="description" bind:value={description}></textarea>
  </div>
  <div>
    <label for="defaultLanguage">Default Language (required):</label>
    <select id="defaultLanguage" bind:value={defaultLanguage} required>
      <option value="" disabled>Select a language</option>
      <option value="en">English</option>
      <option value="de">German</option>
      <option value="fr">French</option>
      <!-- Add more options as needed -->
    </select>
  </div>
  <div>
    <label for="deadline">Optional Deadline:</label>
    <input type="date" id="deadline" bind:value={deadline} />
  </div>
  <div>
    <label for="startDate">Start Date:</label>
    <input type="date" id="startDate" bind:value={startDate} />
  </div>
  <div>
    <label for="endDate">End Date:</label>
    <input type="date" id="endDate" bind:value={endDate} />
  </div>
  <div>
    <label for="participants">Participants (optional):</label>
    <textarea id="participants" bind:value={participants}></textarea>
  </div>
  <button type="submit">Create LearningKit</button>
</form>