/**
 * CLI entry point: async menu for user and task management.
 */

import readline from 'readline';
import * as UserManager from './UserManager.js';
import * as TaskManager from './TaskManager.js';
import { ALL_CATEGORIES } from './Category.js';
import { ALL_STATUSES } from './TaskStatus.js';

/** @type {{ id: string, username: string } | null} */
let currentUser = null;

const rl = readline.createInterface({ input: process.stdin, output: process.stdout });

/**
 * @param {string} prompt
 * @returns {Promise<string>}
 */
function question(prompt) {
  return new Promise((resolve) => rl.question(prompt, resolve));
}

function print(msg) {
  console.log(msg);
}

function printTasks(tasks) {
  if (!tasks.length) {
    print('  (no tasks)');
    return;
  }
  tasks.forEach((t) => {
    print(`  [${t.id}] ${t.title} | ${t.category} | ${t.status} | assignee: ${t.assignee || '-'} | priority: ${t.priority}`);
  });
}

async function mainMenu() {
  print('\n--- Collaborative To-Do ---');
  if (currentUser) {
    print(`Logged in as: ${currentUser.username}`);
    print('  1. List my tasks');
    print('  2. List tasks by category');
    print('  3. List tasks by status');
    print('  4. List tasks assigned to me');
    print('  5. Create task');
    print('  6. Update task');
    print('  7. Delete task');
    print('  8. Logout');
    print('  0. Exit');
  } else {
    print('  (Log in to create and manage tasks)');
    print('  1. Register');
    print('  2. Login');
    print('  0. Exit');
  }
  const choice = await question('Choice: ');
  return choice.trim();
}

async function handleListMyTasks() {
  const tasks = await TaskManager.list({ createdBy: currentUser.id });
  print('\nMy tasks (created by me):');
  printTasks(tasks);
}

async function handleListByCategory() {
  print(`Categories: ${ALL_CATEGORIES.join(', ')}`);
  const cat = await question('Category: ');
  const tasks = await TaskManager.list({ category: cat.trim().toUpperCase() });
  if (!ALL_CATEGORIES.includes(cat.trim().toUpperCase())) {
    print('Invalid category. Showing all for that input anyway.');
  }
  printTasks(tasks);
}

async function handleListByStatus() {
  print(`Statuses: ${ALL_STATUSES.join(', ')}`);
  const status = await question('Status: ');
  const tasks = await TaskManager.list({ status: status.trim().toUpperCase() });
  printTasks(tasks);
}

async function handleListAssignedToMe() {
  const tasks = await TaskManager.list({ assignee: currentUser.username });
  print('\nTasks assigned to me:');
  printTasks(tasks);
}

async function handleCreateTask() {
  const title = await question('Title: ');
  const description = await question('Description (optional): ');
  print(`Category: ${ALL_CATEGORIES.join(', ')}`);
  const category = (await question('Category: ')).trim().toUpperCase() || 'OTHER';
  const priority = parseInt(await question('Priority (0-10, default 0): '), 10) || 0;
  const assignee = (await question('Assignee username (optional): ')).trim();
  const result = await TaskManager.create({
    title,
    description,
    category,
    priority,
    assignee: assignee || undefined,
    createdBy: currentUser.id,
  });
  if (result.success) {
    print('Task created: ' + result.task.id);
  } else {
    print('Error: ' + result.error);
  }
}

async function handleUpdateTask() {
  const id = await question('Task ID: ');
  const task = await TaskManager.getById(id.trim());
  if (!task) {
    print('Task not found.');
    return;
  }
  print(`Current: ${task.title} | ${task.status} | ${task.category}`);
  const title = await question('New title (Enter to keep): ');
  const status = await question('New status PENDING|IN_PROGRESS|COMPLETED (Enter to keep): ');
  const category = await question('New category (Enter to keep): ');
  const updates = {};
  if (title.trim()) updates.title = title.trim();
  if (status.trim()) updates.status = status.trim().toUpperCase();
  if (category.trim()) updates.category = category.trim().toUpperCase();
  if (Object.keys(updates).length === 0) {
    print('No changes.');
    return;
  }
  const result = await TaskManager.update(id.trim(), updates);
  if (result.success) print('Task updated.');
  else print('Error: ' + result.error);
}

async function handleDeleteTask() {
  const id = await question('Task ID: ');
  const result = await TaskManager.remove(id.trim());
  if (result.success) print('Task deleted.');
  else print('Error: ' + result.error);
}

async function handleRegister() {
  const username = await question('Username: ');
  const password = await question('Password: ');
  const displayName = await question('Display name (optional): ');
  const email = await question('Email (optional): ');
  const result = await UserManager.register(username, password, {
    displayName: displayName.trim() || undefined,
    email: email.trim() || undefined,
  });
  if (result.success) {
    print('Registered. You can now login.');
  } else {
    print('Error: ' + result.error);
  }
}

async function handleLogin() {
  const username = await question('Username: ');
  const password = await question('Password: ');
  const result = await UserManager.login(username, password);
  if (result.success) {
    currentUser = { id: result.user.id, username: result.user.username };
    print('Logged in as ' + result.user.username);
  } else {
    print('Error: ' + result.error);
  }
}

async function run() {
  print('Collaborative To-Do — CLI');
  while (true) {
    const choice = await mainMenu();
    if (choice === '0') {
      print('Bye.');
      rl.close();
      process.exit(0);
    }
    if (currentUser) {
      switch (choice) {
        case '1':
          await handleListMyTasks();
          break;
        case '2':
          await handleListByCategory();
          break;
        case '3':
          await handleListByStatus();
          break;
        case '4':
          await handleListAssignedToMe();
          break;
        case '5':
          await handleCreateTask();
          break;
        case '6':
          await handleUpdateTask();
          break;
        case '7':
          await handleDeleteTask();
          break;
        case '8':
          currentUser = null;
          print('Logged out.');
          break;
        default:
          print('Invalid option.');
      }
    } else {
      switch (choice) {
        case '1':
          await handleRegister();
          break;
        case '2':
          await handleLogin();
          break;
        default:
          print('Invalid option.');
      }
    }
  }
}

run().catch((err) => {
  console.error(err);
  process.exit(1);
});
