class ExpertsController < ApplicationController
  def index
    @experts = Expert.all
    
  end

  def show
    @expert = Expert.find(params[:id])
  end

  def new
    @expert = Expert.new
    @procedures = Procedure.all
  end

  def edit
    @expert = Expert.find(params[:id])
    @procedures = Procedure.all
  end

  def create
    @expert = Expert.new(expert_params)
    @expert.save

    extractExpProcedure

    if @expert.save
      redirect_to @expert
    else
      render 'new'
    end
  end

  def update
    @expert = Expert.find(params[:id])

    extractExpProcedure

    if @expert.update(expert_params)
      redirect_to @expert
    else
      render 'edit'
    end
  end

  def destroy
    @expert = Expert.find(params[:id])
    @expert.destroy

    redirect_to experts_path
  end


  private
    def expert_params
      params.require(:expert).permit(:firstName, :lastName,
        :lastName, :secondName, :description, :phoneNumber)
    end

    def procedure_params
      params.require(:procedure)
    end

    def extractExpProcedure
      @expProcedure = {}
      @expProcedure[:expert_id] = @expert[:id]
      procedure_params.each do |procedure|
        if procedure[1][:price].present? then
          @expProcedure[:procedure_id] = procedure[1][:id]
          @expProcedure[:price] = procedure[1][:price]
          ExpProcedure.new(@expProcedure).save
        end
      end
    end
end
