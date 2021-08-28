package de.springframework.springpetclinic.services.map;

import de.springframework.springpetclinic.model.Owner;
import de.springframework.springpetclinic.model.Pet;
import de.springframework.springpetclinic.services.OwnerService;
import de.springframework.springpetclinic.services.PetService;
import de.springframework.springpetclinic.services.PetTypeService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetTypeService petTypeService;
    private final PetService petService;

    public OwnerServiceMap(PetTypeService petTypeService, PetService petService) {
        this.petTypeService = petTypeService;
        this.petService = petService;
    }

    @Override
    public Set<Owner> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public void delete(Owner owner) {
        super.delete(owner);
    }

    // On save the owner gets an id and the pet, too. Is inherited from AbstractMapService.
    @Override
    public Owner save(Owner owner) {
        // Owner has pets
        if(owner != null) {
            if(owner.getPets() != null){
                owner.getPets().forEach(pet -> {
                    // Each pet needs to have PetType before it can be saved
                    if(pet.getPetType() != null) {
                        pet.setPetType(petTypeService.save(pet.getPetType()));
                    } else {
                        throw new RuntimeException("PetType is required.");
                    }
                    // Keep Ids of Pets of Owners and the PetService in sync.
                    if(pet.getId() == null) {
                        // PetService's save method returns a pet object with id property set. All pets are saved in the PetService hashmap.
                        Pet savedPet = petService.save(pet);
                        // The pet of the owner gets a unique pet id.
                        pet.setId(savedPet.getId());
                    }
                });
            }
            return super.save(owner);
        }
        return null;
    }

    @Override
    public Owner findById(Long id) {
        return super.findById(id);
    }

    // Does this belong here?
    public Owner findByLastName(String lastName) {
        if (map.containsValue(lastName)) {
            return map.get(lastName);
        }
        return null;
    }
}
