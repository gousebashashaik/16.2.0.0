define('tui/widget/search/repository/UserRepository', [
  'dojo/json',
  'dojo/store/util/QueryResults',
  'dojo/store/util/SimpleQueryEngine'
], function (JSON, QueryResults, SimpleQueryEngine) {

  function UserRepository() {

  }

  UserRepository.prototype.get = function (id) {
    var id = id || null;
    if (id) {
      return JSON.parse(localStorage.getItem(id)) || null;
    }
  }

  UserRepository.prototype.remove = function (id) {
    var id = id || null;
    if (localStorage[id]) {
      localStorage.removeItem(id);
    }
  }

  UserRepository.prototype.put = function (object, options) {
    var id = options && options.id;
    if (typeof(Storage) !== "undefined") {
      if (localStorage[id])
        localStorage.removeItem(id);
    }
    try {
    localStorage.setItem(id, object);
  }
    catch (err) {
      console.log('Local storage is not supported in your browser')
    }
  }

	UserRepository.prototype.sessionGet = function (id){
		var id = id || null;
		if (id) {
			return JSON.parse(sessionStorage.getItem(id)) || null;
		}
	}

	UserRepository.prototype.sessionPut = function (object, options){
		var id = options && options.id;
		if (typeof(Storage) !== "undefined") {
			if (sessionStorage[id])
				sessionStorage.removeItem(id);
		}
		try {
			sessionStorage.setItem(id, object);
		}
		catch (err) {
			console.log('Local storage is not supported in your browser')
		}
	}

  return new UserRepository();

})