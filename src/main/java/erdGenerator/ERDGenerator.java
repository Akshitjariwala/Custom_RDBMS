package erdGenerator;

public class ERDGenerator {
    public String createERD() {
        StringBuilder sb = new StringBuilder();
        // Create ERD
        sb.append("");
        /*
        SAMPLE GENERATED ERD

        [Person]
        *name
        height
        weight
        +birth_location_id

        [Location]
        *id
        city
        state
        country

        # Cardinality    Syntax
        # 0 or 1         0
        # exactly 1      1
        # 0 or more      *
        # 1 or more      +

        Example:
        Person *--1 Location

         */
        return sb.toString();
    }
}
