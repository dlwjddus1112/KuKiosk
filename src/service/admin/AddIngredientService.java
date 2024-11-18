package service.admin;

import entity.Ingredient;
import repository.IngredientRepository;

import java.util.List;
import java.util.Scanner;

public class AddIngredientService {
    public void start(){
        Scanner sc = new Scanner(System.in);
        System.out.print("추가할 재료명을 입력해주세요 : ");
        var ingredientName = sc.nextLine().trim();
        validateIngredientName(ingredientName);
        System.out.print("재료의 최대 수량을 입력해주세요 : ");
        var ingredientQuantityInput = sc.nextLine().trim();
        try{
            int QuantityInt = Integer.parseInt(ingredientQuantityInput);
        }catch (NumberFormatException e){
            System.out.println("숫자 형식으로 입력해주세요.");
            new AdminService().start();
        }
        int ingredientQuantity = Integer.parseInt(ingredientQuantityInput);
        System.out.print("입력하신 재료를 추가하시겠습니까? (y/n)");
        String answer = sc.nextLine().trim();
        if(answer.equals("y")){
            IngredientRepository.getInstance().addIngredient(ingredientName,ingredientQuantity);
            System.out.println(ingredientName+"가 추가되었습니다. ");
        }
        else if(answer.equals("n")){
            System.out.println("재료 추가를 취소합니다. 관리자 메뉴로 돌아갑니다.");
            new AdminService().start();
        }
        else{
            System.out.println("y 또는 n만 입력해주세요");
            new AdminService().start();
        }

        new AdminService().start();
    }

    private void validateIngredientName(String ingredientName) {
        if(IngredientRepository.getInstance().findByIngredientName(ingredientName) != null){
            System.out.println("이미 존재하는 재료입니다.");
            new AdminService().start();
        }
        if(!ingredientName.matches("^[가-힣 ]{1,5}$")){
            System.out.println("재료 형식에 맞게 입력해주세요.");
            new AdminService().start();
        }
    }


    private void printMenu(){

        System.out.println("----------------------");
        System.out.println("       재료 추가      ");
        System.out.println("----------------------");


    }
}
