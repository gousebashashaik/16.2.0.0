/**
 * Houses all the mappers needed to retrieve the data from the JSON data supplied by the Java layer.
 */
define("tui/widget/maps/Mappers", ["dojo", "dojo/_base/lang"], function (dojo, lang) {

  //Should move this else where.

  _.mixin({
    flatten_arrays: function (object) {
      var flattened = {};
      _.each(_.keys(object), function (key) {
        if (_.isArray(object[key])) {
          flattened[key] = object[key][0]
        }
        else {
          flattened[key] = object[key];
        }
      });
      return flattened;
    }
  });

  function exists(object, attributes) {
    return _.all(attributes, function (attribute) {
      return _.has(object, attribute) && !_.isEmpty(object[attribute]);
    });
  }

  function validLocations(locations) {
    return _.filter(locations, function (location) {
      return !_.isEmpty(location) && !_.isEmpty(location.featureCodesAndValues) &&
          exists(location.featureCodesAndValues, ["longitude", "latitude", "name"])
    });
  }

  function validCatgoryMapData(categoryMapDatas) {
    return _.filter(categoryMapDatas, function (categoryMapData) {
      return !_.isEmpty(categoryMapData) && !_.isEmpty(categoryMapData.title),
          !_.isEmpty(categoryMapData.markerMapViewDataList)
    });
  }

  function makeImageTag(src, name, w, h) {
    return ["<img src='", src, "' alt='", name, "' width='", w, "'>"].join("");
  }

  function newEmptyObject() {
    if ('create' in Object) {
      return Object.create(null);
    } else {
      return {};
    }
  }

  /**
   * Maps the top location name from the JSON data.
   */
  dojo.declare("tui.widget.maps.mappers.TopLocationMapper", null, {
    /**
     * Maps the JSON object with the structure:
     * {"topLocationName":"Costa Blanca", "superCategoryNames":["Valencia", "Spain"] ,........}
     *
     * @param source
     * @returns the concatenation of the two strings present in the attribute "superCategoryNames"
     */
    map: function (source) {
      var mapper = this;
      var locationNameKey = "topLocationName";
      return _.isArray(source[locationNameKey]) ? source[locationNameKey].join(", ") : source[locationNameKey];
    }

  });

  dojo.declare("tui.widget.maps.mappers.FilterLocationTypeMapper", null, {
    /**
     * Maps the JSON object with the structure:
     * {"topLocationName":"Costa Blanca", "superCategoryNames":["Valencia", "Spain"] ,........}
     *
     * @param source
     * @returns the location Type to distinguish between accomm/resort levels
     */
    map: function (source) {
      var mapper = this;
      var locationTypeKey = "locationType";
      return source[locationTypeKey];
    }
  });

  /**
   * Maps the top location from the JSON data.
   */
  dojo.declare("tui.widget.maps.mappers.LocationMapper", null, {
    /**
     * Maps the JSON object with the structure:
     * {"topLocationName":"Costa Blanca", "superCategoryNames":["Valencia", "Spain"] ,........}
     *
     * @param source
     */
    map: function (source, type) {
      var mapper = this;
      var location = source[type];
      location.id = dojox.uuid.generateRandomUuid();
      var thumbnailMapper = lang.getObject("tui.widget.maps.mappers.ThumbnailMapper").call().map;
      thumbnailMapper(location);
      if (location.thumbnail) {
        location.thumbnail.src = makeImageTag(location.thumbnail.mainSrc, location.name, '105');
        location.thumbnail.popSrc = makeImageTag(location.thumbnail.mainSrc, location.name, '232');
      }

      var diffProductMapper = lang.getObject("tui.widget.maps.mappers.DiffProductMapper").call().map;
      diffProductMapper(location);

      if (location.superCategoryNames) {
        location.destinationBreadcrumb = _.isArray(location.superCategoryNames) ? location.superCategoryNames.join(", ") : location.superCategoryNames;
      }
      location.featureCodesAndValues = _.flatten_arrays(location.featureCodesAndValues);
      return location;
    }

  });

  /**
   * Maps the top location from the JSON data.
   */
  dojo.declare("tui.widget.maps.mappers.LocationsMapper", null, {
    /**
     * Maps the JSON object with the structure:
     * {"topLocationName":"Costa Blanca", "superCategoryNames":["Valencia", "Spain"] ,........}
     *
     * @param source
     */
    map: function (source, type) {
      var mapper = this;
      var locations = source[type];
      return _.map(validLocations(locations), function (location) {
        location.id = dojox.uuid.generateRandomUuid();
        location.type = type;
        var locationNameMapper = lang.getObject("tui.widget.maps.mappers.TopLocationMapper").call().map;

        // thumbnail mapper
        var thumbnailMapper = lang.getObject("tui.widget.maps.mappers.ThumbnailMapper").call().map;
        thumbnailMapper(location);
        if (location.thumbnail) {
          location.thumbnail.src = makeImageTag(location.thumbnail.mainSrc, location.name, '105');
          location.thumbnail.popSrc = makeImageTag(location.thumbnail.mainSrc, location.name, '232');
        }

        // mapper for diff mapper
        var diffProductMapper = lang.getObject("tui.widget.maps.mappers.DiffProductMapper").call().map;
        diffProductMapper(location);

        location.destinationBreadcrumb = locationNameMapper(source);
        location.featureCodesAndValues = _.flatten_arrays(location.featureCodesAndValues);
        return location;
      });
    }
  });

  dojo.declare("tui.widget.maps.mappers.ThumbnailMapper", null, {

    map: function (entity) {
      var mapper = this;
      if (exists(entity, ["galleryImages"])) {
        for (var i = 0; i < entity.galleryImages.length; i++) {
          if (entity.galleryImages[i].size === "small") {
            entity.thumbnail = entity.galleryImages[i];
            break;
          }
        }
      }
    }
  });

  dojo.declare("tui.widget.maps.mappers.DiffProductMapper", null, {

    map: function (entity) {
      var mapper = this;
      if (exists(entity, ["productRanges"])) {
        entity.productRanges = entity.productRanges[0];
      } else {
        delete entity.productRanges;
      }
    }
  });

  /**
   */
  dojo.declare("tui.widget.maps.mappers.FilterTypesMapper", null, {
    /**
     * Retrieves the various filter types available in the JSON response.
     *
     * @param source
     */
    map: function (source) {
      var mapper = this;
      var allTypes = ["locations", "accommodations", "hotels", "villas", "events", "sights", "excursions"];
      var availableTypes = _.filter(allTypes, function (type) {
        return _.some(source[type], function (entity) {
          return entity.type === type;
        });
      });
      return _.map(availableTypes, function (element) {
        return {type: element, name: element};
      });
    }

  });

  /**
   */
  dojo.declare("tui.widget.maps.mappers.TopxMapper", null, {

    /**
     * Retrieves the various filter types available in the JSON response.
     *
     * @param source
     */
    map: function (jsonData) {
      var mapper = this;
      var allCategoryMapData = jsonData.inspirationMapViewDataList;

      var locationMapper = lang.getObject("tui.widget.maps.mappers.LocationMapper").call().map;

      //var allTypes = ["locations","events","sights","accommodations","excursions"];
      var allTypes = ["location"];
      var categories = [];
      var defaultLocations = [];
      var defaultCategory = "";

      _.each(validCatgoryMapData(allCategoryMapData), function (categoryMapData, index) {
        var category = {};
        category.name = categoryMapData.title;
        category.type = dojox.dtl.filter.strings.cut(category.name, " ");
        category.pictureUrl = makeImageTag(categoryMapData.pictureUrl, category.name, '250');
        category.count = categoryMapData.markupListCount;
        var categoryLocations = [];
        _.each(categoryMapData.markerMapViewDataList, function (markerMapViewData) {
          _.each(allTypes, function (type) {
            var typedLocations = locationMapper(markerMapViewData, type);
            categoryLocations = categoryLocations.concat(typedLocations);
          });
        });

        //Note: JSON data modified to group locations based on category (beaches, sunny etc..)
        jsonData[category.type] = categoryLocations;

        categories.push(category);
        if (index === 0) {
          defaultCategory = category.type;
          category.active = (index === 0) ? "active" : " ";
          defaultLocations = categoryLocations;
        }

      });
      return {selectedLocations: defaultLocations, allCategories: categories, selectedCategory: defaultCategory};
    }
  });

  dojo.declare("tui.widget.maps.mappers.ThumbnailSet", null, {
    map: function (jsonData, n) {
      var processedGalleryImages = {};
      var setNumber = 0;
      if (!_.has(jsonData, 'galleryImages')) _.debug('Error! jsonData is missing galleryImages object', true);
      if (n) {
        _.each(jsonData.galleryImages, function (galleryImage) {
          if (!_.has(processedGalleryImages, galleryImage.code)) {
            if (setNumber < n) {
              processedGalleryImages[galleryImage.code] = {};
              setNumber++;
            } else {
              return;
            }
          }
          processedGalleryImages[galleryImage.code][galleryImage.size] = galleryImage;
        });
      }
      else {
        _.each(jsonData.galleryImages, function (galleryImage) {
          if (!_.has(processedGalleryImages, galleryImage.code)) {
            processedGalleryImages[galleryImage.code] = {};
          }
          processedGalleryImages[galleryImage.code][galleryImage.size] = galleryImage;
        });
      }
      jsonData.galleryImages = processedGalleryImages;

      return jsonData;
    }
  });

  return {
    //TODO:should memoize.
    locationNameMapper: lang.getObject("tui.widget.maps.mappers.TopLocationMapper").call().map,
    locationsMapper: lang.getObject("tui.widget.maps.mappers.LocationsMapper").call().map,
    filterTypeMapper: lang.getObject("tui.widget.maps.mappers.FilterTypesMapper").call().map,
    topxMapper: lang.getObject("tui.widget.maps.mappers.TopxMapper").call().map,
    filterLocationTypeMapper: lang.getObject("tui.widget.maps.mappers.FilterLocationTypeMapper").call().map,
    thumbnailSet: lang.getObject("tui.widget.maps.mappers.ThumbnailSet").call().map
  };

});