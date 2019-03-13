public static List<JSONParsable> getObjects(final Environment environment, final JSONObject elementObj) throws SettingsParserException{
    final Class<?> elementKlass;
    try{
        elementKlass = Class.forName(Optional.of(elementObj.optString("class")).filter(s -> !s.isBlank()).orElseThrow(() -> new IllegalStateException("No class name provided")));
    }
    catch(final ClassNotFoundException e){
        LOGGER.error("Class from {} not found", elementObj, e);
        throw new IllegalArgumentException("JSON for isn't valid");
    }
    if(!getAllExtendedOrImplementedTypesRecursively(elementKlass) .contains(JSONParsable.class)){
        throw new IllegalArgumentException("Element class isn't parsable from JSON");
    }
    final var parsableClazz = (Class<JSONParsable>) elementKlass;
    final var parameters = Optional.ofNullable(elementObj.optJSONObject("parameters")).orElse(new JSONObject());
    return IntStream.range(0, Optional.of(elementObj.optInt("count")).filter(i -> i > 0).orElse(1)).mapToObj(i -> {
        try{
            return (JSONParsable) parsableClazz.getConstructor(Environment.class) .newInstance(environment).fillFromJson(environment, parameters);
        }
        catch(final IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
            throw new SettingsParserException(parsableClazz, elementObj, e);
        }
    }).collect(Collectors.toList());
}
