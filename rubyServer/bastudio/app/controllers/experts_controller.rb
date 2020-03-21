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
  end

  def create
    @expert = Expert.new(expert_params)
    # render plain: ExpProcedure.qwer
    # @expProc = ExpProcedure.new(expert_id: '1', procedure_id: params[:procedure]['8']['id'], price: '10')
    # render plain: @expProc.save
    if @expert.save
      redirect_to @expert
    else
      render 'new'
    end
  end

  def update
    @expert = Expert.find(params[:id])

    if @expert.update(expert_param)
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
end
