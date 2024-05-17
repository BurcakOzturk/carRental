package business;

import core.Helper;
import dao.ModelDao;
import entity.Model;

import java.util.ArrayList;

public class ModelManager {

    private final ModelDao modelDao = new ModelDao();

    public Model getById(int id) {
        return this.modelDao.getById(id);
    }

    public ArrayList<Model> findAll() {
        return this.modelDao.findAll();
    }

    public ArrayList<Object[]> getForTable(int size, ArrayList<Model> modelList) {
        ArrayList<Object[]> modelObjList = new ArrayList<>();
        for (Model obj : modelList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getId();
            rowObject[i++] = obj.getBrand().getName();
            rowObject[i++] = obj.getName();
            rowObject[i++] = obj.getType();
            rowObject[i++] = obj.getYear();
            rowObject[i++] = obj.getFuel();
            rowObject[i++] = obj.getTransmission();
            modelObjList.add(rowObject);
        }
        return modelObjList;
    }

    public boolean save(Model model) {
        if (this.getById(model.getId()) != null) {
            Helper.showMessage("error");
            return false;
        }
        return this.modelDao.save(model);
    }

    public boolean update(Model model) {
        if (this.getById(model.getId()) == null) {
            Helper.showMessage("Model with " + model.getId() + " could not be found.");
            return false;
        }
        return this.modelDao.update(model);
    }

    public boolean delete(int id) {
        if (this.getById(id) == null) {
            Helper.showMessage("Model with " + id + " could not be found.");
            return false;
        }
        return this.modelDao.delete(id);
    }

    public ArrayList<Model> getByListBrandId(int brandId) {
        return this.modelDao.getByListBrandId(brandId);
    }

    public ArrayList<Model> searchForTable(int brandId, Model.Fuel fuel, Model.Transmission transmission, Model.Type type){
        String select = "SELECT * FROM public.model";
        ArrayList<String> whereList = new ArrayList<>();

        if(brandId != 0){
            whereList.add("model_brand_id = " + brandId);
        }

        if (fuel != null){
            whereList.add("model_fuel ='" + fuel.toString() + "'");
        }

        if (transmission != null){
            whereList.add("model_transmission ='" + transmission.toString() + "'");
        }

        if (type != null){
            whereList.add("model_type ='" + type.toString() + "'");
        }

        String whereStr = String.join(" AND ", whereList);
        String query = select;
        if(!whereStr.isEmpty()){
            query += " WHERE " + whereStr;
        }
        return this.modelDao.selectByQuery(query);
    }
}
