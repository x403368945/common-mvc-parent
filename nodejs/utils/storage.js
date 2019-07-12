export const session = {
  set: (key, value) => {
    if (value === undefined || value === null || value === '') sessionStorage.remove(key);
    else {
      if (typeof value === 'string' || value instanceof String) {
        sessionStorage.setItem(key, value)
      } else {
        sessionStorage.setItem(key, JSON.stringify(value))
      }
    }
  },
  get: (key) => {
    return JSON.parse(sessionStorage.getItem(key));
  },
  has: (key) => {
    return sessionStorage.getItem(key) !== null;
  },
  remove: (key) => {
    sessionStorage.removeItem(key)
  }
};
export const local = {
  set: (key, value) => {
    if (value === undefined || value === null || value === '') localStorage.remove(key);
    else {
      if (typeof value === 'string' || value instanceof String) {
        localStorage.setItem(key, value)
      } else {
        console.log('localStorage');
        console.log(value);
        localStorage.setItem(key, JSON.stringify(value))
      }
    }
  },
  get: (key) => {
    return JSON.parse(localStorage.getItem(key));
  },
  has: (key) => {
    return localStorage.getItem(key) !== null;
  },
  remove: (key) => {
    localStorage.removeItem(key)
  }
};
